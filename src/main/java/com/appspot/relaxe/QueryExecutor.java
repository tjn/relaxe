/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.expr.CountFunction;
import com.appspot.relaxe.expr.DefaultTableExpression;
import com.appspot.relaxe.expr.From;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Limit;
import com.appspot.relaxe.expr.NestedTableReference;
import com.appspot.relaxe.expr.Offset;
import com.appspot.relaxe.expr.OrderBy;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.TableExpression;
import com.appspot.relaxe.query.Query;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryTime;
import com.appspot.relaxe.value.ValueHolder;


public class QueryExecutor {

	private static Logger logger = LoggerFactory.getLogger(QueryExecutor.class);
	
	private PersistenceContext<?> persistenceContext;

	public QueryExecutor(PersistenceContext<?> persistenceContext) {
		super();
		this.persistenceContext = persistenceContext;
	}
	
	public DataObjectQueryResult<DataObject> execute(QueryExpression qe, FetchOptions opts, Connection c) throws QueryException, SQLException {
		
		PersistenceContext<?> pc = getPersistenceContext();
		
		SliceStatement ss = createStatement(qe, opts, c);
		SelectStatement statement = ss.getStatement();
		
		ValueExtractorFactory vef = pc.getValueExtractorFactory();
		List<DataObject> dest = new ArrayList<DataObject>();		
		DataObjectReader r = new DataObjectReader(vef, statement, dest);
			
		StatementExecutor sx = new StatementExecutor(pc);
				
		Query q = new Query(statement);		
		QueryTime qt = sx.execute(statement, c, r);
		
		DataObject.MetaData meta = r.getMetaData();		

		DataObjectQueryResult<DataObject> result = new DataObjectQueryResult<DataObject>(q, meta, dest, qt, opts, ss.getPosition());
		result.setAvailable(ss.getAvailable());
		
		return result;			
	}
	

	public SliceStatement createStatement(QueryExpression qe, FetchOptions opts, Connection c) 
		throws QueryException, SQLException {
		
		PersistenceContext<?> pc = getPersistenceContext();	
		StatementExecutor sx = new StatementExecutor(pc);
		
	//	if (logger().isDebugEnabled()) {
	//		logger().debug("execute: query=" + qe.generate());
	//	}		
				
		SelectStatement qs = new SelectStatement(qe.getTableExpr());
	
		Long available = null;			
		
		long oo = 0;
		Integer pageSize = null;
	
		if (opts != null) {
			oo = opts.getOffset();
			pageSize = opts.getCount();			
		}
				
		logger().debug("execute: oo={}", oo);
				
		if ((opts != null && opts.getCardinality()) || oo < 0) {			
			SelectStatement cs = createCountQuery(qs);
			DataObject result = sx.fetchFirst(cs, c);
			
			ValueHolder<?, ?, ?> h = result.get(0);
			logger().debug("execute: h={}", h);
			available = h.asLongHolder().value();
		}
		
		logger().debug("execute: available={}", available);
		
		Limit limit = (pageSize == null) ? null : new Limit(pageSize.intValue());
		
		long op = oo;
		
		if (oo < 0) {
			op = available.longValue();
						
			if (pageSize != null) {
				long ps = pageSize.longValue();
				long pp = op % ps;
				
				if (pp == 0) {
					op -= ps;
				}
				else {
					op -= pp;
				}
			}			
		}
		
		// TODO: do something with this
		OrderBy ob = qe.getOrderBy();
		
		if (ob == null) {
			ob = new OrderBy(new OrderBy.OrdinalSortKey(1));			
		}		
		 
		Offset offset = new Offset(op);
		qs = new SelectStatement(qe.getTableExpr(), ob, limit, offset);
				
		return new SliceStatement(qs, available, op);
	}
	
	public static class SliceStatement {
		private SelectStatement statement;
		private Long available;
		private long position;
		
		protected SliceStatement(SelectStatement statement, Long available, long position) {
			super();
			this.statement = statement;
			this.available = available;
			this.position = position;
		}
		
		public Long getAvailable() {
			return available;
		}
		
		public long getPosition() {
			return position;
		}
		
		public SelectStatement getStatement() {
			return statement;
		}
	}

	
	private static Logger logger() {
		return QueryExecutor.logger;
	}
	
	public PersistenceContext<?> getPersistenceContext() {
		return persistenceContext;
	}
	
	private SelectStatement createCountQuery(SelectStatement qs) {
		TableExpression te = qs.getTableExpr();	
				

//		Some RDBMS's may whine if same column name appears more than once in sub-query. 
//		(http://bugs.mysql.com/bug.php?id=6709):
		Select select = new Select(IntLiteral.ONE);
				
		QueryExpression tx = new DefaultTableExpression(select, te.getFrom(), te.getWhere(), te.getGroupBy());
		
		final NestedTableReference nt = new NestedTableReference(tx);												
				
		From from = new From(nt);
		DefaultTableExpression ce = new DefaultTableExpression(new Select(new CountFunction()), from);
		
		SelectStatement cs = new SelectStatement(ce.getQueryExpression());
		return cs;
	}
}

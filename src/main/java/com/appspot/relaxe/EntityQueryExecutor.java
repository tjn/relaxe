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

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.DefaultEntityQueryResult;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.ent.query.EntityQueryExpressionBuilder;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.query.Query;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryTime;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;


public class EntityQueryExecutor<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>,
	RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
> 	
{	
	private static Logger logger = LoggerFactory.getLogger(EntityQueryExecutor.class);
	
	private QueryExecutor executor;	
	private UnificationContext unificationContext;
	
	public EntityQueryExecutor(PersistenceContext<?> persistenceContext, UnificationContext unificationContext) {
		super();
		executor = new QueryExecutor(persistenceContext);
		this.unificationContext = unificationContext;
	}

	public EntityQueryResult<A, R, T, E, B, H, F, M, RE> execute(EntityQuery<A, R, T, E, B, H, F, M, RE> query, FetchOptions opts, Connection c) 
		throws SQLException, QueryException, EntityException {
		
		logger().debug("execute: query: " + query);
		
		QueryExecutor se = getExecutor();		
		PersistenceContext<?> pc = se.getPersistenceContext();
		
		List<EntityDataObject<E>> content = new ArrayList<EntityDataObject<E>>();
				
		logger().debug("execute: create reader...");
		
		EntityQueryExpressionBuilder<A, R, T, E, B, H, F, M, RE> eqb = newBuilder(query);
		QueryExpression qe = eqb.getQueryExpression();		
		
		EntityReader<?, ?, ?, ?, ?, ?, ?, ?, ?> eb = 
				new EntityReader<A, R, T, E, B, H, F, M, RE>(pc.getValueExtractorFactory(), eqb, content, this.unificationContext);
								
		QueryExecutor.SliceStatement sb = se.createStatement(qe, opts, c);
						
		StatementExecutor sx = new StatementExecutor(pc);
		
		SelectStatement ss = sb.getStatement();
		Query q = new Query(ss);		
		logger().debug("execute: executing statement...");
		QueryTime qt = sx.executeSelect(ss, c, eb);
		logger().debug("execute: qt: " + qt.getExecutionTime());
		
		DataObject.MetaData meta = eb.getMetaData();		

		DataObjectQueryResult<EntityDataObject<E>> result = 
				new DataObjectQueryResult<EntityDataObject<E>>(q, meta, content, qt, opts, sb.getPosition());
		
		result.setAvailable(sb.getAvailable());
		
		DefaultEntityQueryResult<A, R, T, E, B, H, F, M, RE> eqres = 
				new DefaultEntityQueryResult<A, R, T, E, B, H, F, M, RE>(query, result);
		
		return eqres;
	}
	
	
	public EntityQueryExpressionBuilder<A, R, T, E, B, H, F, M, RE> newBuilder(EntityQuery<A, R, T, E, B, H, F, M, RE> query) 
			throws QueryException, EntityException {
			
		EntityQueryExpressionBuilder<A, R, T, E, B, H, F, M, RE> xb = new EntityQueryExpressionBuilder<A, R, T, E, B, H, F, M, RE>(query);
				
		return xb;
	}

//	private SelectStatement createCountQuery(SelectStatement qs) {
//		TableExpression te = qs.getTableExpr();			
//		QueryExpression tx = new DefaultTableExpression(te);						
//		final NestedTableReference nt = new NestedTableReference(tx);												
//		DefaultTableExpression ce = new DefaultTableExpression();
//		
//		From from = new From(nt);			
//		ce.setFrom(from);
//		
//		Select s = new Select();
//		s.add(new CountFunction());			
//		ce.setSelect(s);
//		
//		SelectStatement cs = new SelectStatement(ce.getQueryExpression());
//		return cs;
//	}
	
//	public Implementation getImplementation() {
//		return implementation;
//	}
	
//	private static Logger logger() {
//		return EntityQueryExecutor.logger;
//	}	
	
		
//	protected SliceStatement createStatement(QueryExpressionSource qes, FetchOptions opts, Connection c) 
//		throws QueryException, SQLException {
//		Implementation imp = getImplementation();
//		
//		StatementExecutor sx = new StatementExecutor(imp);
//		
////		if (logger().isDebugEnabled()) {
////			logger().debug("execute: query=" + qe.generate());
////		}
//		
//		QueryExpression qe = qes.getQueryExpression();		
//		SelectStatement qs = new SelectStatement(qe.getTableExpr());
//
//		Long available = null;			
//		
//		long oo = 0;
//		Long pageSize = null;
//
//		if (opts != null) {
//			oo = opts.getOffset();
//			pageSize = opts.getPageSize();			
//	s	}
//				
//		logger().info("execute: oo=" + oo);
//				
//		if ((opts != null && opts.getCardinality()) || oo < 0) {			
//			SelectStatement cs = createCountQuery(qs);
//			DataObject result = sx.fetchFirst(cs, c);
//			
//			AbstractPrimitiveHolder<?, ?> h = result.get(0);
//			logger().debug("execute: h=" + h);
//			available = h.asLongHolder().value();
//		}
//		
//		logger().info("execute: available=" + available);
//		
//		Limit limit = (pageSize == null) ? null : new Limit(pageSize.longValue());
//		
//		long op = oo;
//		
//		if (oo < 0) {
//			op = available.longValue();
//						
//			if (pageSize != null) {
//				long ps = pageSize.longValue();
//				long pp = op % ps;
//				
//				if (pp == 0) {
//					op -= ps;
//				}
//				else {
//					op -= pp;
//				}
//			}			
//		}		
//		
//		
//		// TODO: do something with this
//		OrderBy ob = qe.getOrderBy();
//		
//		if (ob == null) {
//			ob = new OrderBy();
//			ob.add(new OrderBy.OrdinalSortKey(1));
//		}		
//		 
//		Offset offset = new Offset(op);
//		qs = new SelectStatement(qe.getTableExpr(), ob, limit, offset);
//		
//			
//		return new SliceStatement(qs, available, op);
//	}
		
	public static class SliceStatement2 {
		private SelectStatement statement;
		private Long available;
		private long position;
		
		protected SliceStatement2(SelectStatement statement, Long available, long position) {
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
	
	private QueryExecutor getExecutor() {
		return executor;
	}
	
	private static Logger logger() {
		return EntityQueryExecutor.logger;
	}	
}

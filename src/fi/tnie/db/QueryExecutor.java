/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.expr.CountFunction;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Limit;
import fi.tnie.db.expr.NestedTableReference;
import fi.tnie.db.expr.Offset;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.expr.TableExpression;
import fi.tnie.db.query.Query;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryTime;
import fi.tnie.db.rpc.PrimitiveHolder;

public class QueryExecutor {

	private static Logger logger = Logger.getLogger(QueryExecutor.class);
	
	private PersistenceContext<?> persistenceContext;

	public QueryExecutor(PersistenceContext<?> persistenceContext) {
		super();
		this.persistenceContext = persistenceContext;
	}
	
	public DataObjectQueryResult<DataObject> execute(QueryExpressionSource qes, FetchOptions opts, Connection c) throws QueryException, SQLException {
		
		PersistenceContext<?> pc = getPersistenceContext();
		
		SliceStatement ss = createStatement(qes, opts, c);
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
	

	public SliceStatement createStatement(QueryExpressionSource qes, FetchOptions opts, Connection c) 
		throws QueryException, SQLException {
		
		PersistenceContext<?> pc = getPersistenceContext();	
		StatementExecutor sx = new StatementExecutor(pc);
		
	//	if (logger().isDebugEnabled()) {
	//		logger().debug("execute: query=" + qe.generate());
	//	}
		
		QueryExpression qe = qes.getQueryExpression();		
		SelectStatement qs = new SelectStatement(qe.getTableExpr());
	
		Long available = null;			
		
		long oo = 0;
		Integer pageSize = null;
	
		if (opts != null) {
			oo = opts.getOffset();
			pageSize = opts.getCount();			
		}
				
		logger().info("execute: oo=" + oo);
				
		if ((opts != null && opts.getCardinality()) || oo < 0) {			
			SelectStatement cs = createCountQuery(qs);
			DataObject result = sx.fetchFirst(cs, c);
			
			PrimitiveHolder<?, ?, ?> h = result.get(0);
			logger().debug("execute: h=" + h);
			available = h.asLongHolder().value();
		}
		
		logger().info("execute: available=" + available);
		
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
			ob = new OrderBy();
			ob.add(new OrderBy.OrdinalSortKey(1));
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
		QueryExpression tx = new DefaultTableExpression(te);						
		final NestedTableReference nt = new NestedTableReference(tx);												
		DefaultTableExpression ce = new DefaultTableExpression();
		
		From from = new From(nt);			
		ce.setFrom(from);
		
		Select s = new Select();
		s.add(new CountFunction());			
		ce.setSelect(s);
		
		SelectStatement cs = new SelectStatement(ce.getQueryExpression());
		return cs;
	}
}

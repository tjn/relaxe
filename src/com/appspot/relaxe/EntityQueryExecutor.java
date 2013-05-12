/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.DefaultEntityQueryResult;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.EntityQueryTemplate;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.query.Query;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryTime;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public class EntityQueryExecutor<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
> 	
{	
//	private Implementation implementation;	
//	private static Logger logger = Logger.getLogger(EntityQueryExecutor.class);
	
	private QueryExecutor executor;	
	private UnificationContext unificationContext;
	
	public EntityQueryExecutor(PersistenceContext<?> persistenceContext, UnificationContext unificationContext) {
		super();
		executor = new QueryExecutor(persistenceContext);
		this.unificationContext = unificationContext;
	}

	public EntityQueryResult<A, R, T, E, H, F, M, C, QT> execute(EntityQuery<A, R, T, E, H, F, M, C, QT> query, FetchOptions opts, Connection c) 
		throws SQLException, QueryException, EntityException {
		
		QueryExecutor se = getExecutor();		
		PersistenceContext<?> pc = se.getPersistenceContext();
		
		List<EntityDataObject<E>> content = new ArrayList<EntityDataObject<E>>();
		
		EntityReader<?, ?, ?, ?, ?, ?, ?, ?> eb = new EntityReader<A, R, T, E, H, F, M, C>(pc.getValueExtractorFactory(), query, content, this.unificationContext);
		
		QueryExecutor.SliceStatement sb = se.createStatement(query, opts, c);
				
		StatementExecutor sx = new StatementExecutor(pc);
		
		SelectStatement ss = sb.getStatement();
		Query q = new Query(ss);		
		QueryTime qt = sx.execute(ss, c, eb);
		
		DataObject.MetaData meta = eb.getMetaData();		

		DataObjectQueryResult<EntityDataObject<E>> result = new DataObjectQueryResult<EntityDataObject<E>>(q, meta, content, qt, opts, sb.getPosition());
		result.setAvailable(sb.getAvailable());
		
		return new DefaultEntityQueryResult<A, R, T, E, H, F, M, C, QT>(query, result);		
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
//		}
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
}

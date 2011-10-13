/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.DefaultEntityQueryResult;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
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
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityQueryExecutor<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>
> {	
	private Implementation implementation;	
	private static Logger logger = Logger.getLogger(EntityQueryExecutor.class);
	
	public EntityQueryExecutor(Implementation implementation) {
		super();
		this.implementation = implementation;
	}

	public EntityQueryResult<A, R, T, E, H, F, M, QT> execute(EntityQuery<A, R, T, E, H, F, M, QT> query, FetchOptions opts, Connection c) 
		throws SQLException, QueryException, EntityException {
		
		Implementation imp = getImplementation();
		// QueryExpression qe = query.getQueryExpression();
		QueryExpression qe = query.getQueryExpression();
					
//		ValueExtractorFactory vef = imp.getValueExtractorFactory();
		
		List<EntityDataObject<E>> content = new ArrayList<EntityDataObject<E>>();		
		EntityReader<?, ?, ?, ?, ?, ?, ?> eb = new EntityReader<A, R, T, E, H, F, M>(imp, query, content);
		StatementExecutor sx = new StatementExecutor(imp);
	
		if (logger().isDebugEnabled()) {
			logger().debug("execute: query=" + qe.generate());
		}
		
		SelectStatement qs = new SelectStatement(qe.getTableExpr());

		Long available = null;
		
		// boolean card = opts.getCardinality() ||
		
		long oo = 0;
		Long pageSize = null;

		if (opts != null) {
			oo = opts.getOffset();
			pageSize = opts.getPageSize();			
		}
				
		logger().info("execute: oo=" + oo);
				
		if ((opts != null && opts.getCardinality()) || oo < 0) {			
			SelectStatement cs = createCountQuery(qs);
			DataObject result = sx.fetchFirst(cs, c);
			
			PrimitiveHolder<?, ?> h = result.get(0);
			logger().debug("execute: h=" + h);
			available = h.asLongHolder().value();
		}
		
		logger().info("execute: available=" + available);
		
		Limit limit = (pageSize == null) ? null : new Limit(pageSize.longValue());
		
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
										
		Query q = new Query(qs);
		QueryTime qt = sx.execute(qs, c, eb);
		
		DataObject.MetaData meta = eb.getMetaData();		
				
		// QueryResult<EntityDataObject<E>> result = new QueryResult<EntityDataObject<E>>(q, content, qt);
		DataObjectQueryResult<EntityDataObject<E>> result = new DataObjectQueryResult<EntityDataObject<E>>(q, meta, content, qt, opts, op);
		result.setAvailable(available);
		
		return new DefaultEntityQueryResult<A, R, T, E, H, F, M, QT>(query, result);		
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
	
	public Implementation getImplementation() {
		return implementation;
	}
	
	private static Logger logger() {
		return EntityQueryExecutor.logger;
	}	
}

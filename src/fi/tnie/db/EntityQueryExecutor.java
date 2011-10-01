/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.exec.QueryProcessorAdapter;
import fi.tnie.db.expr.AllColumns;
import fi.tnie.db.expr.CountFunction;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.NestedSelect;
import fi.tnie.db.expr.NestedTableReference;
import fi.tnie.db.expr.AbstractQueryExpression;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.expr.TableExpression;
import fi.tnie.db.expr.TableRefList;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.query.Query;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.query.QueryTime;
import fi.tnie.db.rpc.LongHolder;
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
	M extends EntityMetaData<A, R, T, E, H, F, M>
> {	
	private Implementation implementation;	
	private static Logger logger = Logger.getLogger(EntityQueryExecutor.class);
	
	public EntityQueryExecutor(Implementation implementation) {
		super();
		this.implementation = implementation;
	}




	public QueryResult<EntityDataObject<E>> execute(EntityQuery<A, R, T, E, M> query, boolean rowCount, Connection c) 
		throws SQLException, QueryException, EntityException {
		
		Implementation imp = getImplementation();
		QueryExpression qe = query.getQueryExpression();		
		ValueExtractorFactory vef = imp.getValueExtractorFactory();
		
		List<EntityDataObject<E>> content = new ArrayList<EntityDataObject<E>>();		
		EntityReader<?, ?, ?, ?, ?, ?, ?> eb = new EntityReader<A, R, T, E, H, F, M>(vef, query, content);
		StatementExecutor sx = new StatementExecutor(imp);
	
		if (logger().isDebugEnabled()) {
			logger().debug("execute: query=" + qe.generate());
		}
		
		SelectStatement qs = new SelectStatement(qe);				
		Query q = new Query(qs);
		QueryTime qt = sx.execute(qs, c, eb);
		
		Long available = null;
		
		if (rowCount) {
			
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
			DataObject result = sx.fetchFirst(cs, c);
			
			PrimitiveHolder<?, ?> h = result.get(0);
			logger().debug("execute: h=" + h);
			available = h.asLongHolder().value();			
			logger().info("execute: available=" + available);
		}
		
				
		QueryResult<EntityDataObject<E>> result = new QueryResult<EntityDataObject<E>>(q, content, qt);
		result.setAvailable(available);
		return result;
	}
	
	public Implementation getImplementation() {
		return implementation;
	}
	
	private static Logger logger() {
		return EntityQueryExecutor.logger;
	}	
}

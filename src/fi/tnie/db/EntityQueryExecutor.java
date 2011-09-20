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
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.query.Query;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.query.QueryTime;
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




	public QueryResult<EntityDataObject<E>> execute(EntityQuery<A, R, T, E, M> query, Connection c) 
		throws SQLException, QueryException {
		
		QueryExpression qe = query.getQueryExpression();
		
		ValueExtractorFactory vef = getImplementation().getValueExtractorFactory();
		
		List<EntityDataObject<E>> content = new ArrayList<EntityDataObject<E>>();		
		EntityReader<?, ?, ?, ?, ?, ?, ?> eb = new EntityReader<A, R, T, E, H, F, M>(vef, query, content);
		StatementExecutor sx = new StatementExecutor();
	
		if (logger().isDebugEnabled()) {
			logger().debug("execute: query=" + qe.generate());
		}
		
		
		Query q = new Query(qe);			
		QueryTime qt = sx.execute(qe, c, eb);							
		QueryResult<EntityDataObject<E>> result = new QueryResult<EntityDataObject<E>>(q, content, qt);		
		return result;
	}
	
	public Implementation getImplementation() {
		return implementation;
	}
	
	private static Logger logger() {
		return EntityQueryExecutor.logger;
	}	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.service;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public interface QueryService {


	public <
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends fi.tnie.db.ent.Content,
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
	>
	EntityQueryResult<A, R, T, E, H, F, M, C, QT> executeQuery(EntityQuery<A, R, T, E, H, F, M, C, QT> query, FetchOptions opts)
		throws EntityException;
	
	DataObjectQueryResult<DataObject> executeQuery(QueryExpressionSource qes, FetchOptions opts)
		throws QueryException;

}

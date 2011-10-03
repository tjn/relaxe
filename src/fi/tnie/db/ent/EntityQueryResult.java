/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.query.QueryResult;
import fi.tnie.db.types.ReferenceType;

public interface EntityQueryResult<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, ?, ?, M>,	
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends Response<EntityQuery<A, R, T, E, M>> 
{
	QueryResult<EntityDataObject<E>> getContent();	
}
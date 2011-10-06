/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.query.QueryResult;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public interface EntityQueryResult<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,	
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,	
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>
>
	extends Response<EntityQuery<A, R, T, E, H, F, M, QT>> 
{
	DataObjectQueryResult<EntityDataObject<E>> getContent();
		
	
}

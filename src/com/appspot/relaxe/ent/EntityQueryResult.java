/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.paging.HasDataObjectQueryResult;
import com.appspot.relaxe.paging.ResultPage;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;

public interface EntityQueryResult<
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
	extends Response<EntityQuery<A, R, T, E, H, F, M, C, QT>>, ResultPage, HasDataObjectQueryResult<EntityDataObject<E>>
{
	DataObjectQueryResult<EntityDataObject<E>> getContent();
}
/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public interface EntityQueryTemplate<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
> 
	extends	Serializable
{	
	M getMetaData();
	EntityQuery<A, R, T, E, H, F, M, C, RE> newQuery();
	
	// public void addPredicate(EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> q, EntityQueryPredicate<?> p);
}


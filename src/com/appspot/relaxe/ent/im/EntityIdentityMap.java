/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.im;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;

public interface EntityIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, ?, M, ?>,
	E extends Entity<A, R, T, E, H, ?, M, ?>,
	H extends ReferenceHolder<A, R, T, E, H, M, ?>,
	M extends EntityMetaData<A, R, T, E, H, ?, M, ?>
> 
	extends IdentityMap<E, H>
{
}

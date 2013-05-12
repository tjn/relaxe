/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.rpc.ReferenceHolder;

public abstract class ReferenceType<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
>
	extends AbstractType<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7916062747738467637L;

	@Override
	public final boolean isReferenceType() {
		return true;
	}
	
	public abstract M getMetaData();
	
	@Override
	public abstract T self();
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.im;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.AbstractPrimitiveKey;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.ReferenceType;


public class AbstractKeyIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, RH, ?, M, ?>,
	E extends Entity<A, R, T, E, RH, ?, M, ?>,
	RH extends ReferenceHolder<A, R, T, E, RH, M, ?>,
	M extends EntityMetaData<A, R, T, E, RH, ?, M, ?>,
	V extends Serializable,
	P extends AbstractPrimitiveType<P>,
	H extends AbstractPrimitiveHolder<V, P, H>,	
	K extends AbstractPrimitiveKey<A, E, V, P, H, K>
>
	extends AbstractIdentityMap<A, R, T, E, RH, M, V>
{
	private K key;
	
	public AbstractKeyIdentityMap(K key) {
		super();
		
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		this.key = key;
	}
	
	@Override
	protected V identify(E src) 
		throws EntityRuntimeException {
		H h = src.get(this.key);
		return (h == null) ? null : h.value();
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.BooleanHolder;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.BooleanType;
import com.appspot.relaxe.types.AbstractPrimitiveType;

public final class BooleanKey<
	A extends Attribute,
	E extends HasBoolean<A, E>
>
	extends AbstractPrimitiveKey<A, E, Boolean, BooleanType, BooleanHolder, BooleanKey<A, E>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private BooleanKey() {
	}

	private BooleanKey(HasBooleanKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasBoolean<X, T>
	>
	BooleanKey<X, T> get(HasBooleanKey<X, T> meta, X a) {
		BooleanKey<X, T> k = meta.getBooleanKey(a);
		
		if (k == null) {
			AbstractPrimitiveType<?> t = a.type();
			
			if (t != null && BooleanType.TYPE.equals(t)) {
				k = new BooleanKey<X, T>(meta, a);
			}
		}
				
		return k;
	}
		
	@Override
	public BooleanType type() {
		return BooleanType.TYPE;
	}
	
	@Override
	public void set(E e, BooleanHolder newValue) {
		e.setBoolean(this, newValue);
	}
	
	@Override
	public BooleanHolder get(E e) {
		return e.getBoolean(self());
	}
	
	@Override
	public BooleanHolder newHolder(Boolean newValue) {
		return BooleanHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setBoolean(this, src.getBoolean(this));
	}

	@Override
	public BooleanKey<A, E> self() {
		return this;
	}

	@Override
	public void reset(E dest) throws EntityRuntimeException {
		dest.setBoolean(this, BooleanHolder.NULL_HOLDER);
	}
		
	@Override
	public BooleanHolder as(AbstractPrimitiveHolder<?, ?, ?> unknown) {
		return unknown.asBooleanHolder();
	}
}

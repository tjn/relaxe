/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.PrimitiveType;

public final class IntegerKey<
	A extends Attribute,
	E extends HasInteger<A, E>
>
	extends AbstractPrimitiveKey<A, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, E>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private IntegerKey() {
	}

	private IntegerKey(HasIntegerKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasInteger<X, T>
	>
	IntegerKey<X, T> get(HasIntegerKey<X, T> meta, X a) {
		IntegerKey<X, T> k = meta.getIntegerKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && IntegerType.TYPE.equals(t)) {
				k = new IntegerKey<X, T>(meta, a);
			}
		}
				
		return k;
	}
		
	@Override
	public IntegerType type() {
		return IntegerType.TYPE;
	}
	
	public void set(E e, IntegerHolder newValue) {
		e.setInteger(this, newValue);
	}
	
	public IntegerHolder get(E e) {
		return e.getInteger(self());
	}
	
	@Override
	public IntegerHolder newHolder(Integer newValue) {
		return IntegerHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setInteger(this, src.getInteger(this));
	}

	@Override
	public IntegerKey<A, E> self() {
		return this;
	}

	@Override
	public void reset(E dest) throws EntityRuntimeException {
		dest.setInteger(this, IntegerHolder.NULL_HOLDER);
	}
		
	@Override
	public IntegerHolder as(PrimitiveHolder<?, ?, ?> unknown) {
		return unknown.asIntegerHolder();
	}
}

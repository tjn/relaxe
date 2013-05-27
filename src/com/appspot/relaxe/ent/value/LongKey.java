/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.HasKey;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.PrimitiveType;

public final class LongKey<
	A extends Attribute,
	E extends HasLong<A, E>
>
	extends AbstractPrimitiveKey<A, E, Long, LongType, LongHolder, LongKey<A, E>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private LongKey() {
	}

	private LongKey(HasLongKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasLong<X, T> & HasKey<LongType, LongKey<X, T>, T>
	>
	LongKey<X, T> get(HasLongKey<X, T> meta, X a) {
		LongKey<X, T> k = meta.getLongKey(a);
		
		if (k == null) {
			AbstractPrimitiveType<?> t = a.type();
			
			if (t != null && t.getSqlType() == PrimitiveType.INTEGER) {
				k = new LongKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}
		
	@Override
	public LongType type() {
		return LongType.TYPE;
	}
	
	@Override
	public void set(E e, LongHolder newValue) 
		throws EntityRuntimeException {
		e.setLong(this, newValue);
	}
	
	@Override
	public LongHolder get(E e) 
		throws EntityRuntimeException {
		return e.getLong(self());
	}
	
	@Override
	public LongHolder newHolder(Long newValue) {
		return LongHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setLong(this, src.getLong(this));
	}

	@Override
	public LongKey<A, E> self() {
		return this;
	}

	@Override
	public void reset(E dest) throws EntityRuntimeException {
		dest.setLong(this, LongHolder.NULL_HOLDER);
	}
	
	@Override
	public LongHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return LongHolder.of(holder);
	}
}

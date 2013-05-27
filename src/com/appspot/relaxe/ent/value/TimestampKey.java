/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.util.Date;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.TimestampHolder;
import com.appspot.relaxe.types.TimestampType;


public final class TimestampKey<	
	A extends Attribute,
	E extends HasTimestamp<A, E>
>
	extends AbstractPrimitiveKey<A, E, Date, TimestampType, TimestampHolder, TimestampKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7215861304511882107L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimestampKey() {
	}

	private TimestampKey(HasTimestampKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasTimestamp<X, T>
	>
	TimestampKey<X, T> get(HasTimestampKey<X, T> meta, X a) {
		TimestampKey<X, T> k = meta.getTimestampKey(a);
		
		if (k == null) {						
			if (TimestampType.TYPE.equals(a.type())) {
				k = new TimestampKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public TimestampType type() {
		return TimestampType.TYPE;
	}

	@Override
	public void set(E e, TimestampHolder newValue) 
		throws EntityRuntimeException {
		e.setTimestamp(this, newValue);
	}
	
	@Override
	public TimestampHolder get(E e) 
		throws EntityRuntimeException {
		return e.getTimestamp(this);
	}
	
	@Override
	public TimestampHolder newHolder(Date newValue) {
		return TimestampHolder.valueOf(newValue);
	}
	
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {		
		dest.setTimestamp(this, src.getTimestamp(this));		
	}

	@Override
	public TimestampKey<A, E> self() {
		return this;
	}

	@Override
	public TimestampHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return TimestampHolder.of(holder);
	}
}

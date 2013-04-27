/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.util.Date;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.DateHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.DateType;
import com.appspot.relaxe.types.PrimitiveType;


public final class DateKey<	
	A extends Attribute,	
	E extends HasDate<A, E>
>
	extends AbstractPrimitiveKey<A, E, Date, DateType, DateHolder, DateKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = -8003688473297829554L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DateKey() {
	}

	private DateKey(HasDateKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasDate<X, T>
	>
	DateKey<X, T> get(HasDateKey<X, T> meta, X a) {
		DateKey<X, T> k = meta.getDateKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && DateType.TYPE.equals(t)) {
				k = new DateKey<X, T>(meta, a);
			}
		}
				
		return k;
	}

	
	@Override
	public DateType type() {
		return DateType.TYPE;
	}

	public void set(E e, DateHolder newValue) 
		throws EntityRuntimeException {
		e.setDate(this, newValue);
	}
	
	public DateHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDate(this);
	}
	
	@Override
	public DateHolder newHolder(Date newValue) {
		return DateHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, E dest)
		throws EntityRuntimeException {
		dest.setDate(this, src.getDate(this));
	}

	@Override
	public DateKey<A, E> self() {
		return this;		
	}
	
	@Override
	public DateHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return DateHolder.as(holder);
	}
}

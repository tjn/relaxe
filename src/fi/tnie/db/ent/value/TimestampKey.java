/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.TimestampType;

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

	public void set(E e, TimestampHolder newValue) 
		throws EntityRuntimeException {
		e.setTimestamp(this, newValue);
	}
	
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

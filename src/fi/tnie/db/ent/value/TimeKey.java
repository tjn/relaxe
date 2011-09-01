/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.TimeType;

public final class TimeKey<
	A extends Attribute, 
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveKey<A, T, E, Date, TimeType, TimeHolder, TimeKey<A, T, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7215861304511882107L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimeKey() {
	}

	private TimeKey(EntityMetaData<A, ?, T, E, ?, ?, ?> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Z extends ReferenceType<Z, ?>,
		T extends Entity<X, ?, Z, T, ?, ?, ?>
	>
	TimeKey<X, Z, T> get(EntityMetaData<X, ?, Z, T, ?, ?, ?> meta, X a) {
		TimeKey<X, Z, T> k = meta.getTimeKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.TIME) {
				k = new TimeKey<X, Z, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public TimeType type() {
		return TimeType.TYPE;
	}

	public void set(E e, TimeHolder newValue) 
		throws EntityRuntimeException {
		e.setTime(this, newValue);
	}
	
	public TimeHolder get(E e) 
		throws EntityRuntimeException {
		return e.getTime(this);
	}
	
	@Override
	public TimeHolder newHolder(Date newValue) {
		return TimeHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setTime(this, src.getTime(this));		
	}

	@Override
	public TimeKey<A, T, E> self() {
		return this;
	}

}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.TimeType;

public final class TimeKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Date, TimeType, TimeHolder, E, TimeKey<A, E>>
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

	private TimeKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	TimeKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		TimeKey<X, T> k = meta.getTimeKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.TIME) {
				k = new TimeKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public TimeType type() {
		return TimeType.TYPE;
	}

	public void set(E e, TimeHolder newValue) {
		e.setTime(this, newValue);
	}
	
	public TimeHolder get(E e) {
		return e.getTime(this);
	}
	
	@Override
	public TimeHolder newHolder(Date newValue) {
		return TimeHolder.valueOf(newValue);
	}
	
//	@Override
//	public TimeKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
//		return meta.getTimeKey(name());
//	}
	
}

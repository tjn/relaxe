/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.TimestampType;

public final class TimestampKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Date, TimestampType, TimestampHolder, E, TimestampKey<A, E>>
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

	private TimestampKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	TimestampKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		TimestampKey<X, T> k = meta.getTimestampKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.TIMESTAMP) {
				k = new TimestampKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public TimestampType type() {
		return TimestampType.TYPE;
	}

	public void set(E e, TimestampHolder newValue) {
		e.setTimestamp(this, newValue);
	}
	
	public TimestampHolder get(E e) {
		return e.getTimestamp(this);
	}
	
	@Override
	public TimestampHolder newHolder(Date newValue) {
		return TimestampHolder.valueOf(newValue);
	}
	
	@Override
	public TimestampKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
		return meta.getTimestampKey(name());
	}
	
}

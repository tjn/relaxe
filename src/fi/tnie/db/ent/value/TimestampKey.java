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
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.TimestampType;

public final class TimestampKey<	
	A extends Attribute, 
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>>
	extends AbstractPrimitiveKey<A, R, T, E, Date, TimestampType, TimestampHolder, TimestampKey<A, R, T, E>>
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

	private TimestampKey(EntityMetaData<A, R, T, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Y, 
		Z extends ReferenceType<Z>,		
		T extends Entity<X, Y, Z, T>
	>
	TimestampKey<X, Y, Z, T> get(EntityMetaData<X, Y, Z, T> meta, X a) {
		TimestampKey<X, Y, Z, T> k = meta.getTimestampKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.TIMESTAMP) {
				k = new TimestampKey<X, Y, Z, T>(meta, a);
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
	public void copy(E src, E dest) {
		dest.setTimestamp(this, src.getTimestamp(this));		
	}

	@Override
	public TimestampKey<A, R, T, E> self() {
		return this;
	}
}

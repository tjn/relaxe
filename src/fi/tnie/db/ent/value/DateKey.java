/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.Type;

public final class DateKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Date, DateType, DateHolder, E, DateKey<A, E>>
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

	private DateKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	DateKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		DateKey<X, T> k = meta.getDateKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == Type.DATE) {
				k = new DateKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}
	
	@Override
	public DateType type() {
		return DateType.TYPE;
	}

	public void set(E e, DateHolder newValue) {
		e.setDate(this, newValue);
	}
	
	public DateHolder get(E e) {
		return e.getDate(this);
	}
	
	@Override
	public DateHolder newHolder(Date newValue) {
		return DateHolder.valueOf(newValue);
	}

	@Override
	public DateKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {		
		return meta.getDateKey(name());
	}

}

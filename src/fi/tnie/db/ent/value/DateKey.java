/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class DateKey<	
	A extends Attribute, 
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>>
	extends AbstractPrimitiveKey<A, R, T, E, Date, DateType, DateHolder, DateKey<A, R, T, E>>
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

	private DateKey(EntityMetaData<A, R, T, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Y extends Reference, 
		Z extends ReferenceType<Z>,
		T extends Entity<X, Y, Z, T>
	>
	DateKey<X, Y, Z, T> get(EntityMetaData<X, Y, Z, T> meta, X a) {
		DateKey<X, Y, Z, T> k = meta.getDateKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t == DateType.TYPE) {
				k = new DateKey<X, Y, Z, T>(meta, a);
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
	public void copy(E src, E dest) {
		dest.setDate(this, src.getDate(this));
	}

	@Override
	public DateKey<A, R, T, E> self() {
		return this;		
	}
}

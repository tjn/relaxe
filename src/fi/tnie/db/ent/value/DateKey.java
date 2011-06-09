/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.model.ent.EntityModel;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class DateKey<	
	A extends Attribute,
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveKey<A, T, E, Date, DateType, DateHolder, DateKey<A, T, E>>
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

	private DateKey(EntityMetaData<A, ?, T, E, ?, ?, ?> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Z extends ReferenceType<Z, ?>,
		T extends Entity<X, ?, Z, T, ?, ?, ?>
	>
	DateKey<X, Z, T> get(EntityMetaData<X, ?, Z, T, ?, ?, ?> meta, X a) {
		DateKey<X, Z, T> k = meta.getDateKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t == DateType.TYPE) {
				k = new DateKey<X, Z, T>(meta, a);
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
	public DateKey<A, T, E> self() {
		return this;		
	}
	
	@Override
	public ValueModel<DateHolder> getAttributeModel(EntityModel<A, T, E, ?> m) {
		return m.getDateModel(this);
	}
}

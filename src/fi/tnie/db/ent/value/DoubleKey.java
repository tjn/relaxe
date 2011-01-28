/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;
import fi.tnie.db.types.PrimitiveType;

public final class DoubleKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Double, DoubleType, DoubleHolder, E, DoubleKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1065150474303051699L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DoubleKey() {
	}

	private DoubleKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	DoubleKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		DoubleKey<X, T> k = meta.getDoubleKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.DOUBLE) {
				k = new DoubleKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DoubleType type() {
		return DoubleType.TYPE;
	}

	public void set(E e, DoubleHolder newValue) {
		e.setDouble(this, newValue);
	}
	
	public DoubleHolder get(E e) {
		return e.getDouble(this);
	}

	@Override
	public DoubleHolder newHolder(Double newValue) {
		return DoubleHolder.valueOf(newValue);
	}
	
	@Override
	public DoubleKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
		return meta.getDoubleKey(name());
	}
		
}

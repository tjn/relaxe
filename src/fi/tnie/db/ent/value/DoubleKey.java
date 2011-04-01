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
import fi.tnie.db.types.ReferenceType;

public final class DoubleKey<	
	A extends Attribute, 
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>>
	extends AbstractPrimitiveKey<A, R, T, E, Double, DoubleType, DoubleHolder, DoubleKey<A, R, T, E>>
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

	private DoubleKey(EntityMetaData<A, R, T, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Y, 
		Z extends ReferenceType<Z>,		
		T extends Entity<X, Y, Z, T>
	>
	DoubleKey<X, Y, Z, T> get(EntityMetaData<X, Y, Z, T> meta, X a) {
		DoubleKey<X, Y, Z, T> k = meta.getDoubleKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.DOUBLE) {
				k = new DoubleKey<X, Y, Z, T>(meta, a);
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
	public void copy(E src, E dest) {
		dest.setDouble(this, src.getDouble(this));
	}
	
	@Override
	public DoubleKey<A, R, T, E> self() {
		return this;
	}

}

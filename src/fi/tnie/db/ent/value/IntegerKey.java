/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class IntegerKey<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AbstractPrimitiveKey<A, R, T, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, R, T, E>>
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private IntegerKey() {
	}

	private IntegerKey(EntityMetaData<A, R, T, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Y extends Reference, 
		Z extends ReferenceType<Z>,
		T extends Entity<X, Y, Z, T>
	>
	IntegerKey<X, Y, Z, T> get(EntityMetaData<X, Y, Z, T> meta, X a) {
		IntegerKey<X, Y, Z, T> k = meta.getIntegerKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && t.getSqlType() == PrimitiveType.INTEGER) {
				k = new IntegerKey<X, Y, Z, T>(meta, a);
			}			
		}
				
		return k;
	}
		
	@Override
	public IntegerType type() {
		return IntegerType.TYPE;
	}
	
	public void set(E e, IntegerHolder newValue) {
		e.setInteger(this, newValue);
	}
	
	public IntegerHolder get(E e) {
		return e.getInteger(this);
	}
	
	@Override
	public IntegerHolder newHolder(Integer newValue) {
		return IntegerHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setInteger(this, src.getInteger(this));
	}

	@Override
	public IntegerKey<A, R, T, E> self() {
		return this;
	}
}

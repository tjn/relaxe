/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class IntegerKey<
	A extends Attribute,	
	T extends ReferenceType<A, ?, T, E, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveKey<A, T, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, T, E>>
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

	private IntegerKey(EntityMetaData<A, ?, T, E, ?, ?, ?> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,		 
		Z extends ReferenceType<X, ?, Z, T, ?, ?, ?>,
		T extends Entity<X, ?, Z, T, ?, ?, ?>
	>
	IntegerKey<X, Z, T> get(EntityMetaData<X, ?, Z, T, ?, ?, ?> meta, X a) {
		IntegerKey<X, Z, T> k = meta.getIntegerKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && t.getSqlType() == PrimitiveType.INTEGER) {
				k = new IntegerKey<X, Z, T>(meta, a);
			}			
		}
				
		return k;
	}
		
	@Override
	public IntegerType type() {
		return IntegerType.TYPE;
	}
	
	public void set(E e, IntegerHolder newValue) 
		throws EntityRuntimeException {
		e.setInteger(this, newValue);
	}
	
	public IntegerHolder get(E e) 
		throws EntityRuntimeException {
		return e.getInteger(this);
	}
	
	@Override
	public IntegerHolder newHolder(Integer newValue) {
		return IntegerHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setInteger(this, src.getInteger(this));
	}

	@Override
	public IntegerKey<A, T, E> self() {
		return this;
	}
}

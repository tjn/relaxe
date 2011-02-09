/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.PrimitiveType;

public final class IntegerKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Integer, IntegerType, IntegerHolder, E, IntegerKey<A, E>>
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

	private IntegerKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	IntegerKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		IntegerKey<X, T> k = meta.getIntegerKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && t.getSqlType() == PrimitiveType.INTEGER) {
				k = new IntegerKey<X, T>(meta, a);
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
	
//	@Override
//	public IntegerKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
//		return meta.getIntegerKey(name());
//	}
}

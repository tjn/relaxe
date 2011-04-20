/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.types.DecimalType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class DecimalKey<
	A extends Attribute, 
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AbstractPrimitiveKey<A, R, T, E, Decimal, DecimalType, DecimalHolder, DecimalKey<A, R, T, E>>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1332513199153161810L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DecimalKey() {
	}

	private DecimalKey(EntityMetaData<A, R, T, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Y extends Reference, 
		Z extends ReferenceType<Z>,
		T extends Entity<X, Y, Z, T>
	>
	DecimalKey<X, Y, Z, T> get(EntityMetaData<X, Y, Z, T> meta, X a) {
		DecimalKey<X, Y, Z, T> k = meta.getDecimalKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.DOUBLE) {
				k = new DecimalKey<X, Y, Z, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DecimalType type() {
		return DecimalType.TYPE;
	}

	public void set(E e, DecimalHolder newValue) {
		e.setDecimal(this, newValue);
	}
	
	public DecimalHolder get(E e) {
		return e.getDecimal(this);
	}
	
	@Override
	public void copy(E src, E dest) {
		dest.setDecimal(this, src.getDecimal(this));
	}

	@Override
	public DecimalHolder newHolder(Decimal newValue) {
		return DecimalHolder.valueOf(newValue);
	}
	
	@Override
	public DecimalKey<A, R, T, E> self() {
		return this;
	}
}

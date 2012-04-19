/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.types.DecimalType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class DecimalKey<
	A extends Attribute, 
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractPrimitiveKey<A, T, E, Decimal, DecimalType, DecimalHolder, DecimalKey<A, T, E>>
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

	private DecimalKey(EntityMetaData<A, ?, T, E, ?, ?, ?, ?> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		Z extends ReferenceType<X, ?, Z, T, ?, ?, ?, ?>,
		T extends Entity<X, ?, Z, T, ?, ?, ?, ?>
	>
	DecimalKey<X, Z, T> get(EntityMetaData<X, ?, Z, T, ?, ?, ?, ?> meta, X a) {
		DecimalKey<X, Z, T> k = meta.getDecimalKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.DOUBLE) {
				k = new DecimalKey<X, Z, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DecimalType type() {
		return DecimalType.TYPE;
	}

	public void set(E e, DecimalHolder newValue)
		throws EntityRuntimeException {
		e.setDecimal(this, newValue);
	}
	
	public DecimalHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDecimal(this);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDecimal(this, src.getDecimal(this));
	}

	@Override
	public DecimalHolder newHolder(Decimal newValue) {
		return DecimalHolder.valueOf(newValue);
	}
	
	@Override
	public DecimalKey<A, T, E> self() {
		return this;
	}
}

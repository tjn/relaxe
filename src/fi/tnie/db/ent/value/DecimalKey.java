/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.types.DecimalType;
import fi.tnie.db.types.PrimitiveType;

public final class DecimalKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, Decimal, DecimalType, DecimalHolder, E, DecimalKey<A, E>>
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

	private DecimalKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	DecimalKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		DecimalKey<X, T> k = meta.getDecimalKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.DOUBLE) {
				k = new DecimalKey<X, T>(meta, a);
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
	public DecimalHolder newHolder(Decimal newValue) {
		return DecimalHolder.valueOf(newValue);
	}	
}

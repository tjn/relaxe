/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.types.DecimalType;
import fi.tnie.db.types.ReferenceType;

public class DecimalAccessor<
	A extends Attribute,	
	T extends ReferenceType<T, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveAccessor<A, T, E, Decimal, DecimalType, DecimalHolder, DecimalKey<A, T, E>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1855077165248914678L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DecimalAccessor() {
	}

	public DecimalAccessor(E target, DecimalKey<A, T, E> k) {
		super(target, k);
	}
}
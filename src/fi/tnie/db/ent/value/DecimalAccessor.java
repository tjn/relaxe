/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.types.DecimalType;

public class DecimalAccessor<
	A extends Attribute,		
	E extends HasDecimal<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, Decimal, DecimalType, DecimalHolder, DecimalKey<A, E>> {

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

	public DecimalAccessor(E target, DecimalKey<A, E> k) {
		super(target, k);
	}
}
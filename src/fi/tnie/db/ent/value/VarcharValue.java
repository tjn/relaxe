/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.VarcharType;

public class VarcharValue<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveValue<A, String, VarcharType, VarcharHolder, E, VarcharKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4034249028279108750L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private VarcharValue() {
	}

	public VarcharValue(E target, VarcharKey<A, E> k) {
		super(target, k); 
	}
}
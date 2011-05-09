/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public class VarcharAccessor<
	A extends Attribute,
	T extends ReferenceType<T, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveAccessor<A, T, E, String, VarcharType, VarcharHolder, VarcharKey<A, T, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4034249028279108750L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private VarcharAccessor() {
	}

	public VarcharAccessor(E target, VarcharKey<A, T, E> k) {
		super(target, k); 
	}
}
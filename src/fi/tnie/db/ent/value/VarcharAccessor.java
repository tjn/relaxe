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
	R,
	T extends ReferenceType<T>,	
	E extends Entity<A, R, T, E>
>
	extends AbstractPrimitiveAccessor<A, R, T, E, String, VarcharType, VarcharHolder, VarcharKey<A, R, T, E>> {

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

	public VarcharAccessor(E target, VarcharKey<A, R, T, E> k) {
		super(target, k); 
	}
}
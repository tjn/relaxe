/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.AbstractPrimitiveAccessor;
import fi.tnie.db.rpc.StringArray;
import fi.tnie.db.test.HasPGTextArray;

public class PGTextArrayAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasPGTextArray<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, StringArray, PGTextArrayType, PGTextArrayHolder, PGTextArrayKey<A, E>> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private PGTextArrayAccessor() {
	}

	public PGTextArrayAccessor(E target, PGTextArrayKey<A, E> k) {
		super(target, k);
	}
}
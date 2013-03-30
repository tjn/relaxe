/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.LongVarBinaryHolder;
import fi.tnie.db.types.LongVarBinaryType;

public class LongVarBinaryAccessor<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?> & HasLongVarBinary<A, E>
>
	extends AbstractPrimitiveAccessor<A, E, LongVarBinary, LongVarBinaryType, LongVarBinaryHolder, LongVarBinaryKey<A, E>> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1725602690923907736L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private LongVarBinaryAccessor() {
	}

	public LongVarBinaryAccessor(E target, LongVarBinaryKey<A, E> k) {
		super(target, k);
	}
}
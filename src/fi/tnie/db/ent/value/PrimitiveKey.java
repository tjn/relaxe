/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public abstract class PrimitiveKey<
	A extends Attribute, V extends Serializable, P extends PrimitiveType<P>, H extends PrimitiveHolder<V, P>,
	E extends Entity<A, ?, ?, E>,
	K extends Key<A, V, P, H, E, K>>
	implements Key<A, V, P, H, E, K> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3422141375386521175L;
	private A name;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected PrimitiveKey() {
	}

	public PrimitiveKey(A name) {
		super();
		this.name = name;
	}

	@Override
	public A name() {
		return this.name;
	}

}

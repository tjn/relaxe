/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public abstract class AbstractValue<
	A extends Attribute,
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P>,
	E extends Entity<A, ?, ?, E>,
	K extends Key<A, S, P, H, E, K>
	>
	implements Value<A, S, P, H, E, K>
{

	/**
	 *
	 */
	private static final long serialVersionUID = 7060596100410117898L;
	private H holder;
	private K key;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractValue() {
	}

	public AbstractValue(K key) {
		super();
		this.key = key;
	}

	public A attribute() {
		return key().name();
	}

	public K key() {
		return this.key;
	}

	public void setHolder(H newHolder) {
		this.holder = newHolder;
	}

	public H getHolder() {
		return this.holder;
	}

	public abstract void set(S newValue);

	public S get() {
		return getHolder().value();
	}

}

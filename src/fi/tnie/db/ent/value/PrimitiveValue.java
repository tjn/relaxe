/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public class PrimitiveValue<
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
	
	private E target;
	private K key;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected PrimitiveValue() {
	}

	public PrimitiveValue(E target, K key) {
		super();
		setTarget(target);
		
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		this.key = key;
	}

	public A attribute() {
		return key().name();
	}

	public K key() {
		return this.key;
	}

	public void setHolder(H newHolder) {
		getTarget().set(this.key, newHolder);
	}

	public H getHolder() {
		return getTarget().get(this.key);		
	}

	public void set(S newValue) {		
		setHolder(this.key.newHolder(newValue));
	}
		
	public S get() {
		return getHolder().value();
	}

	public E getTarget() {
		return target;
	}

	public void setTarget(E target) {
		if (target == null) {
			throw new NullPointerException("target");
		}
		
		this.target = target;
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public class AbstractPrimitiveAccessor<
	A extends Attribute,		
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>,
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P>,	
	K extends PrimitiveKey<A, T, E, S, P, H, K>
	>
	implements PrimitiveAccessor<A, T, E, S, P, H, K>
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
	protected AbstractPrimitiveAccessor() {
	}

	public AbstractPrimitiveAccessor(E target, K key) {
		super();

		if (target == null) {
			throw new NullPointerException("target");
		}		
		
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		this.target = target;
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

	protected void setTarget(E target) {
		if (target == null) {
			throw new NullPointerException("target");
		}
		
		this.target = target;
	}
}

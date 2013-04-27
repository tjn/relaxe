/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public abstract class AbstractPrimitiveAccessor<
	A extends Attribute,
	E,
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P, H>,	
	K extends PrimitiveKey<A, E, S, P, H, K>
	>
	implements PrimitiveAccessor<A, E, S, P, H, K>
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

//	public abstract void setHolder(H newHolder);
	
	public void setHolder(H newHolder) {
		key().set(getTarget(), newHolder);
	}

	public H getHolder() 
		throws EntityRuntimeException {		
		return this.key().get(getTarget());				
	}

	public void set(S newValue) 
		throws EntityRuntimeException {
		setHolder(this.key.newHolder(newValue));
	}
		
	public S get() 
		throws EntityRuntimeException {
		H h = getHolder();
		return (h == null) ? null : h.value();
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

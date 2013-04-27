/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public class PrimitiveStamper<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?>,	
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P, H>,	
	K extends PrimitiveKey<A, E, S, P, H, K>
	>
	extends AbstractPrimitiveAccessor<A, E, S, P, H, K>
	implements Mutator<A, E, S, P, H, K>
{

	/**
	 *
	 */
	private static final long serialVersionUID = 7060596100410117898L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected PrimitiveStamper() {
	}

	public PrimitiveStamper(E target, K key) {
		super(target, key);
	}


	@Override
	public void setHolder(H newHolder) 
		throws EntityRuntimeException {
		getTarget().set(key(), newHolder);
	}

	@Override
	public void set(S newValue) 
		throws EntityRuntimeException {		
		setHolder(key().newHolder(newValue));
	}		
}

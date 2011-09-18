/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public class PrimitiveStamper<
	A extends Attribute,	
	T extends ReferenceType<A, ?, T, E, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>,	
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P>,	
	K extends PrimitiveKey<A, T, E, S, P, H, K>
	>
	extends AbstractPrimitiveAccessor<A, T, E, S, P, H, K>
	implements Mutator<A, T, E, S, P, H, K>
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

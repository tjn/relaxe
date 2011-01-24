/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public class PrimitiveStamper<
	A extends Attribute,
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P>,
	E extends Entity<A, ?, ?, E>,
	K extends Key<A, S, P, H, E, K>
	>
	extends PrimitiveAccessor<A, S, P, H, E, K>
	implements Mutator<A, S, P, H, E, K>
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


	public void setHolder(H newHolder) {
		getTarget().set(key(), newHolder);
	}

	public void set(S newValue) {		
		setHolder(key().newHolder(newValue));
	}		
}

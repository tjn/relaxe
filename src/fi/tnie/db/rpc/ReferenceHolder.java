/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.types.ReferenceType;


public abstract class ReferenceHolder<	
	V extends Entity<?, ?, T, ? extends V>,
	T extends ReferenceType<T>>	
	extends Holder<V, T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7758303666346608268L;
	private V value;
			
	protected ReferenceHolder() {
		super();	
	}

	public ReferenceHolder(V value) {
		super();		
		this.value = value;
	}

	@Override
	public V value() {
		return this.value;
	}
}

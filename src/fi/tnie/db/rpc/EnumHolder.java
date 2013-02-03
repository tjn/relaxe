/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.EnumType;
import fi.tnie.db.types.Enumerable;

public abstract class EnumHolder<V extends Enum<V> & Enumerable, T extends EnumType<T, V>, H extends EnumHolder<V, T, H>>
	extends OtherHolder<V, T, H> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
	
	private V value;
		
	public EnumHolder(V value) {
		super();
		this.value = value;
	}
	
	protected EnumHolder() {
		super();
	}
	
	@Override
	public V value() {
		return this.value;
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

/**
 * 
 * @author tnie
 *
 * @param <V>
 */

public class SimpleValueModel<V>
	extends AbstractValueModel<V> {
	
	private V value;
	
	public SimpleValueModel() {
		this(null);		
	}

	public SimpleValueModel(V initialValue) {
		this.value = initialValue;
	}

	@Override
	public V get() {	
		return value;
	}
	
	
}

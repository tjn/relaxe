/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public abstract class AbstractMutableValueModel<V>
	extends AbstractValueModel<V>
	implements MutableValueModel<V> {

	private V value;
	
	public AbstractMutableValueModel() {		
	}
	
	public AbstractMutableValueModel(V value) {
		super();
		set(value);
	}
	
	protected void apply(V newValue) {
		this.value = newValue;
	}

	@Override
	public V get() {
		return this.value;
	}
	
	@Override
	public MutableValueModel<V> asMutable() {	
		return this;
	}
	
	@Override
	public boolean isNullable() {
		return true;
	}
}
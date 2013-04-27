/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public interface ConstantValueModel<V>
	extends ImmutableValueModel<V> {

	
	/** 
	 * The value of the model. This is guaranteed to return always the same object reference.
	 * 
	 * @return
	 */
	@Override
	public V get();
}

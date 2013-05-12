/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public interface NotNullableModel<V>
	extends MutableValueModel<V> {

	
	/**
	 * @throws NullPointerException If newValue is <code>null</code>.
	 */
	@Override
	public void set(V newValue)
		throws NullPointerException;
	
	/**
	 * Returns <code>false</code>.
	 * @return
	 */
	@Override
	boolean isNullable();
}

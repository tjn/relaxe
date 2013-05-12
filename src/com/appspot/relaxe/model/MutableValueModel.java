/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public interface MutableValueModel<V>
	extends ValueModel<V> {

	void set(V newValue);	
	@Override
	ImmutableValueModel<V> asImmutable();
	
	boolean isNullable();
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public abstract class AbstractImmutableModel<V>
	extends AbstractValueModel<V>
	implements ImmutableValueModel<V> {

	@Override
	public abstract V get();

	
	@Override
	public boolean isMutable() {
		return false;
	}		
}

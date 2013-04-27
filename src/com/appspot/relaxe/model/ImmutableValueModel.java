/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public interface ImmutableValueModel<V>
	extends ValueModel<V> {
	
	/**
	 * Returns <code>null</code>.
	 */	
	@Override
	public MutableValueModel<V> asMutable();	
}

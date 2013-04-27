/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public interface BooleanModel
	extends ValueModel<Boolean> {
	
	@Override
	public MutableBooleanModel asMutable();
	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public abstract class AbstractBooleanModel
	extends AbstractValueModel<Boolean>
	implements BooleanModel
{	
	@Override
	public abstract MutableBooleanModel asMutable();
	
	
}

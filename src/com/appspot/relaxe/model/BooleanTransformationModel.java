/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public abstract class BooleanTransformationModel<S>
	extends AbstractTransformationModel<Boolean, S> 
	implements BooleanModel {

	public BooleanTransformationModel(ValueModel<S> source) {
		super(source);
	}
	
	@Override
	public final MutableBooleanModel asMutable() {
		return null;
	}

}

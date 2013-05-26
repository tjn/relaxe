/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.model;

public class BooleanNotModel
	extends BooleanTransformationModel<Boolean> {

	public BooleanNotModel(ValueModel<Boolean> source) {
		super(source);
	}
	
	@Override
	public Boolean transform(Boolean source) {
		if (source == null) {
			return null;
		}
		
		return Boolean.valueOf(!source.booleanValue());
	}
}
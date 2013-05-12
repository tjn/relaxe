/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class FloatType
	extends AbstractPrimitiveType<FloatType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5928611637725679062L;
	public static final FloatType TYPE = new FloatType();
	
	private FloatType() {		
	}
	
	@Override
	public int getSqlType() {
		return AbstractPrimitiveType.FLOAT;
	}
	
	@Override
	public FloatType self() {
		return this;
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class NumericType
	extends AbstractPrimitiveType<NumericType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5650977569547726690L;
	public static final NumericType TYPE = new NumericType();
	
	private NumericType() {		
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.NUMERIC;
	}
	
	@Override
	public NumericType self() {
		return this;
	}
	
	@Override
	public String getName() {
		return "NUMERIC";
	}
}

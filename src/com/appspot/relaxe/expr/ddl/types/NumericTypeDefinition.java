/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public class NumericTypeDefinition
    extends FixedPrecisionDefinition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5794913835887942942L;

	public NumericTypeDefinition() {
		super();
	}
	
	public NumericTypeDefinition(int precision, int scale) {
		this(Integer.valueOf(precision), Integer.valueOf(scale));
	}
	
	public NumericTypeDefinition(Integer precision, Integer scale) {
		super(precision, scale, SQLTypeDefinition.Name.NUMERIC);
	}	
	
	public NumericTypeDefinition(int precision) {
		super(precision);
	}
	
	public static NumericTypeDefinition get(int precision, int scale) {
		return new NumericTypeDefinition(precision, scale);
	}

	
	public static NumericTypeDefinition get(Integer precision, Integer scale) {
		return new NumericTypeDefinition(precision, scale);
	}


	@Override
	public Name getSQLTypeName() {
		return SQLTypeDefinition.Name.NUMERIC;
	}	
}

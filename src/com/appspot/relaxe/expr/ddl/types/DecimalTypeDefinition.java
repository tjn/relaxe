/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public class DecimalTypeDefinition
    extends FixedPrecisionDefinition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3542857951379437870L;
	
	public DecimalTypeDefinition() {
		super();
	}

	public DecimalTypeDefinition(int precision, int scale) {
		this(Integer.valueOf(precision), Integer.valueOf(scale));
	}
	
	public DecimalTypeDefinition(Integer precision, Integer scale) {
		super(precision, scale, SQLTypeDefinition.Name.DECIMAL);
	}

	public DecimalTypeDefinition(int precision) {
		super(precision);
	}


	@Override
	public Name getSQLTypeName() {
		return SQLTypeDefinition.Name.DECIMAL;
	}
	
	public static DecimalTypeDefinition get(int precision, int scale) {
		return new DecimalTypeDefinition(precision, scale);
	}
	
	public static DecimalTypeDefinition get(Integer precision, Integer scale) {
		return new DecimalTypeDefinition(precision, scale);
	}
}

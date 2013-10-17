/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class IntegerType
	extends AbstractPrimitiveType<IntegerType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6049549759893756694L;
	
	public static final IntegerType TYPE = new IntegerType();
	
	private IntegerType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.INTEGER;
	}
	
	@Override
	public IntegerType self() {
		return this;
	}
	
	@Override
	public String getName() {
		return "INTEGER";
	}
}

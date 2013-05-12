/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class DoubleType
	extends AbstractPrimitiveType<DoubleType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8260606722095946070L;
	public static final DoubleType TYPE = new DoubleType();
	
	private DoubleType() {	
	}
	
	@Override
	public int getSqlType() {
		return AbstractPrimitiveType.DOUBLE;
	}
	
	@Override
	public DoubleType self() {
		return this;
	}
}

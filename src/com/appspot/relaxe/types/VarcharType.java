/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class VarcharType
	extends AbstractPrimitiveType<VarcharType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3335101232181862680L;
	
	public static final VarcharType TYPE = new VarcharType();
	
	protected VarcharType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.VARCHAR;
	}
	
	@Override
	public VarcharType self() {
		return this;
	}
}

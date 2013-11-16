/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public class VarcharArrayType
	extends ArrayType<VarcharArrayType, VarcharType> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 8645143143285784693L;
	
	public static final VarcharArrayType TYPE = new VarcharArrayType();

	@Override
	public VarcharArrayType self() {
		return this;
	}
	
	@Override
	public VarcharType getElementType() {
		return VarcharType.TYPE;
	}

	@Override
	public String getName() {
		return "VARCHAR[]";
	}
}

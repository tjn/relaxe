/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class VarcharArrayType
	extends ArrayType<VarcharArrayType, VarcharType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8645143143285784693L;

	@Override
	public VarcharArrayType self() {
		return this;
	}
	
	@Override
	public VarcharType getElementType() {
		return VarcharType.TYPE;
	}
}

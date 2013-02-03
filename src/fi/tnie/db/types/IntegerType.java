/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class IntegerType
	extends PrimitiveType<IntegerType> {
	
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
}

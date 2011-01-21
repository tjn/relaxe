/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class IntegerType
	extends PrimitiveType<IntegerType> {
	
	public static final IntegerType TYPE = new IntegerType();
	
	private IntegerType() {
	}
	
	@Override
	public int getSqlType() {
		return Type.INTEGER;
	}
}

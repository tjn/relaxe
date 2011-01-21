/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class DoubleType
	extends PrimitiveType<DoubleType> {
	
	public static final DoubleType TYPE = new DoubleType();
	
	private DoubleType() {	
	}
	
	@Override
	public int getSqlType() {
		return Type.DOUBLE;
	}
}

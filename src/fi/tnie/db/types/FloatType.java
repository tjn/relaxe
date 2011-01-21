/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class FloatType
	extends PrimitiveType<FloatType> {
	
	public static final FloatType TYPE = new FloatType();
	
	private FloatType() {		
	}
	
	@Override
	public int getSqlType() {
		return Type.FLOAT;
	}

}

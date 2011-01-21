/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class DecimalType
	extends PrimitiveType<DecimalType> {
	
	public static final DecimalType TYPE = new DecimalType();
	
	private DecimalType() {		
	}
	
	@Override
	public int getSqlType() {
		return Type.DECIMAL;
	}

}

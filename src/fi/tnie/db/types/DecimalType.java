/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class DecimalType
	extends PrimitiveType<DecimalType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5650977569547726690L;
	public static final DecimalType TYPE = new DecimalType();
	
	private DecimalType() {		
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.DECIMAL;
	}
}

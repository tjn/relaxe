/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class FloatType
	extends PrimitiveType<FloatType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5928611637725679062L;
	public static final FloatType TYPE = new FloatType();
	
	private FloatType() {		
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.FLOAT;
	}
	
	@Override
	public FloatType self() {
		return this;
	}
}

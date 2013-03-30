/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class BooleanType
	extends PrimitiveType<BooleanType> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -4585202606752420801L;
	
	public static final BooleanType TYPE = new BooleanType();
	
	private BooleanType() {
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.BOOLEAN;
	}
	
	@Override
	public BooleanType self() {
		return this;
	}
}

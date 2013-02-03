/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class DoubleType
	extends PrimitiveType<DoubleType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8260606722095946070L;
	public static final DoubleType TYPE = new DoubleType();
	
	private DoubleType() {	
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.DOUBLE;
	}
	
	@Override
	public DoubleType self() {
		return this;
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public abstract class FloatingPointDefinition
    extends AbstractNumericTypeDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 2301149943314207588L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	public FloatingPointDefinition() {		
	}
	
    @Override
    public abstract SQLTypeDefinition.Name getSQLTypeName();

	@Override
	public final boolean isExact() {
		return false;
	}
	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public class FloatTypeDefinition
    extends FloatingPointDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -269346141333155275L;

	public static final FloatTypeDefinition DEFINITION = new FloatTypeDefinition();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	private FloatTypeDefinition() {		
	}
	
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {
    	return SQLTypeDefinition.Name.FLOAT;
    }
}

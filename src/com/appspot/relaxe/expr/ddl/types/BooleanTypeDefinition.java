/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public class BooleanTypeDefinition
    extends SQLTypeDefinition {
       	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	private static final BooleanTypeDefinition TYPE = new BooleanTypeDefinition();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private BooleanTypeDefinition() {
	}
		
	public static BooleanTypeDefinition get() {
        return TYPE;
    }   
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {     
        return SQLTypeDefinition.Name.BIT;
    }
}

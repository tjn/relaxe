/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public class TimeTypeDefinition
    extends SQLTypeDefinition {
       	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	private static final TimeTypeDefinition TYPE = new TimeTypeDefinition();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimeTypeDefinition() {
	}
		
	public static TimeTypeDefinition get() {
        return TYPE;
    }   
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {     
        return SQLTypeDefinition.Name.TIME;
    }
}

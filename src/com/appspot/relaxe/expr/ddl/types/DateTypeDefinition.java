/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public class DateTypeDefinition
    extends SQLTypeDefinition {
       	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	private static final DateTypeDefinition TYPE = new DateTypeDefinition();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DateTypeDefinition() {
	}
		
	public static DateTypeDefinition get() {
        return TYPE;
    }   
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {     
        return SQLTypeDefinition.Name.DATE;
    }
}

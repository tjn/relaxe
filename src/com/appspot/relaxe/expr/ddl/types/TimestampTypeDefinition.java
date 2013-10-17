/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

public class TimestampTypeDefinition
    extends SQLTypeDefinition {
        
    /**
	 * 
	 */
	private static final long serialVersionUID = 779228554708816888L;
	
	private static final TimestampTypeDefinition TYPE = new TimestampTypeDefinition();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimestampTypeDefinition() {
	}
		
	public static TimestampTypeDefinition get() {
        return TYPE;
    }   
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {     
        return SQLTypeDefinition.Name.TIMESTAMP;
    }
}

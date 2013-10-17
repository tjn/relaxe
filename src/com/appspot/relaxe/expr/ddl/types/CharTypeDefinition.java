/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public class CharTypeDefinition
    extends AbstractCharacterTypeDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2495916308822328263L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected CharTypeDefinition() {
	}
	
    public CharTypeDefinition(int length) {
        super(Integer.valueOf(length));
    }
    
    public static CharTypeDefinition get(int length) {
        return new CharTypeDefinition(length);
    }
    
    public static CharTypeDefinition get(Integer charOctetLength) {
    	return new CharTypeDefinition(charOctetLength);
    }
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {
        return SQLTypeDefinition.Name.CHAR;
    }
}

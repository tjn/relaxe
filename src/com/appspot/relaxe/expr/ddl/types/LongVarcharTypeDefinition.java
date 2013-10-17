/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public class LongVarcharTypeDefinition
    extends AbstractCharacterTypeDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2495916308822328263L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected LongVarcharTypeDefinition() {
	}
	
    public LongVarcharTypeDefinition(int length) {
        super(Integer.valueOf(length));
    }
    
    public static LongVarcharTypeDefinition get(int length) {
        return new LongVarcharTypeDefinition(length);
    }
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {
        return SQLTypeDefinition.Name.LONGVARCHAR;
    }
}

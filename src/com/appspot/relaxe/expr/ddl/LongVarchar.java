/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

public class LongVarchar 
    extends AbstractCharacterType {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2495916308822328263L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected LongVarchar() {
	}
	
    public LongVarchar(int length) {
        super(Integer.valueOf(length));
    }
    
    public static LongVarchar get(int length) {
        return new LongVarchar(length);
    }
    
    @Override
    public SQLType.Name getSQLTypeName() {
        return SQLType.Name.CHAR;
    }
}

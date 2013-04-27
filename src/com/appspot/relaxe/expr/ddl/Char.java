/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

public class Char 
    extends AbstractCharacterType {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2495916308822328263L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Char() {
	}
	
    public Char(int length) {
        super(length);
    }
    
    @Override
    public SQLType.Name getSQLTypeName() {
        return SQLType.Name.CHAR;
    }
}

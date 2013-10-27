/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public class VarBinaryTypeDefinition
    extends AbstractBinaryTypeDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2495916308822328263L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected VarBinaryTypeDefinition() {
	}
	    
    public VarBinaryTypeDefinition(Integer length) {
        super(length);
    }    
    
    public static VarBinaryTypeDefinition get(Integer length) {
        return new VarBinaryTypeDefinition(length);
    }
    
    public static VarBinaryTypeDefinition get() {
        return new VarBinaryTypeDefinition(null);
    }
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {
        return SQLTypeDefinition.Name.VARBINARY;
    }
}

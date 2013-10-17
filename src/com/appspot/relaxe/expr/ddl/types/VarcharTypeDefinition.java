/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public class VarcharTypeDefinition
    extends AbstractCharacterTypeDefinition {
    
//    public static final Varchar VARCHAR_10 = new Varchar(10);
//    public static final Varchar VARCHAR_20 = new Varchar(20);
//    public static final Varchar VARCHAR_30 = new Varchar(30);
//    public static final Varchar VARCHAR_40 = new Varchar(40);
//    public static final Varchar VARCHAR_50 = new Varchar(50);
//    public static final Varchar VARCHAR_100 = new Varchar(100);	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2301149943314207588L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected VarcharTypeDefinition() {
		
	}
	
    public static VarcharTypeDefinition get(int length) {
        return VarcharTypeDefinition.get(Integer.valueOf(length));
    }
    
    public static VarcharTypeDefinition get(Integer length) {
        return (length == null) ? new VarcharTypeDefinition(null) : new VarcharTypeDefinition(length);
    }
    
    public VarcharTypeDefinition(Integer length) {
        super(length);
    }
    
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {     
        return SQLTypeDefinition.Name.VARCHAR;
    }
}

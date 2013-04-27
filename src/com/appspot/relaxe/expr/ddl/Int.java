/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

public class Int
    extends NumericType {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2776206428444898457L;
	private SQLType.Name name;
    
    enum Type {
        TINY,
        SMALL,
        LONG,
        BIG,        
    }
    
    public Int() {      
        this(null);
    }
    
    public Int(Type t) {
        this.name = SQLType.Name.INTEGER;
    }
    
    @Override
    public boolean isExact() {     
        return true;
    }
    
    @Override
    public SQLType.Name getSQLTypeName() {
        return this.name;
    }

    
    
}

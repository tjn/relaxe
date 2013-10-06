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
		
    
    public enum Type {
        TINY,
        SMALL,
        LONG,
        BIG,        
    }
    
    public static final Int INT = new Int();
    public static final Int TINY_INT = new Int(Int.Type.TINY);
    public static final Int SMALL_INT = new Int(Int.Type.SMALL);
    public static final Int LONG_INT = new Int(Int.Type.LONG);
    public static final Int BIG_INT = new Int(Int.Type.BIG);
    
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

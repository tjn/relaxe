/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;


public abstract class AbstractNumericTypeDefinition
    extends SQLTypeDefinition {            
    /**
	 * 
	 */
	private static final long serialVersionUID = -2780564179649802147L;

	/**
     * True is numeric data type is exact, false if it's approximate. 
     * @return
     */
    public abstract boolean isExact();
    
}

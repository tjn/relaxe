/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

public abstract class NumericType 
    extends SQLType {            
    /**
     * True is numeric data type is exact, false if it's approximate. 
     * @return
     */
    public abstract boolean isExact();
    
}

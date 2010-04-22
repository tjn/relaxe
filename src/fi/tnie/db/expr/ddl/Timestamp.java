/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

public class Timestamp
    extends SQLType {
        
    public static Timestamp get() {
        return new Timestamp();
    }   
    
    @Override
    public SQLTypeName getSQLTypeName() {     
        return SQLTypeName.TIMESTAMP;
    }
}

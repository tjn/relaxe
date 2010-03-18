/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

public class Int
    extends NumericType {
    
    private SQLTypeName name;
    
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
        this.name = SQLTypeName.INTEGER;
    }
    
    @Override
    public boolean isExact() {     
        return true;
    }
    
    @Override
    public SQLTypeName getSQLTypeName() {
        return this.name;
    }

    
    
}

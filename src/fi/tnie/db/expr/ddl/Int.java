/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

public class Int
    extends NumericType {
    
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

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.Element;
import fi.tnie.db.types.Type;

public abstract class SQLType
    extends DataTypeDefinition {

    @Override
    public Element getName() {
        return getSQLTypeName();
    }
    
    public abstract SQLTypeName getSQLTypeName();
    
    
    public static boolean isTextType(int sqltype) {
        return 
            (sqltype == Type.CHAR) || 
            (sqltype == Type.VARCHAR) ||            
            (sqltype == Type.LONGVARCHAR) ||
            (sqltype == Type.NCHAR) ||
            (sqltype == Type.NVARCHAR) ||
            (sqltype == Type.LONGNVARCHAR) ||            
            (sqltype == Type.CLOB)
           ;        
    }
    
    public static boolean isIntegralType(int sqltype) {
        return 
        (sqltype == Type.TINYINT) || 
        (sqltype == Type.INTEGER) ||            
        (sqltype == Type.SMALLINT) ||
        (sqltype == Type.BIGINT)    
       ;        
    }
    
    public static boolean isFixedNumeric(int sqltype) {
        return 
        (sqltype == Type.NUMERIC) || 
        (sqltype == Type.DECIMAL)    
       ;        
    }    
    
    public static boolean isFloatingPoint(int sqltype) {
        return 
        (sqltype == Type.REAL) || 
        (sqltype == Type.FLOAT) ||
        (sqltype == Type.DOUBLE)    
       ;        
    }        
}

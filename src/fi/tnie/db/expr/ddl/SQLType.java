/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import java.sql.Types;

import fi.tnie.db.expr.Element;

public abstract class SQLType
    extends DataTypeDefinition {

    @Override
    public Element getName() {
        return getSQLTypeName();
    }
    
    public abstract SQLTypeName getSQLTypeName();
    
    
    public static boolean isTextType(int sqltype) {
        return 
            (sqltype == Types.CHAR) || 
            (sqltype == Types.VARCHAR) ||            
            (sqltype == Types.LONGVARCHAR) ||
            (sqltype == Types.NCHAR) ||
            (sqltype == Types.NVARCHAR) ||
            (sqltype == Types.LONGNVARCHAR) ||            
            (sqltype == Types.CLOB)
           ;        
    }
    
    public static boolean isIntegralType(int sqltype) {
        return 
        (sqltype == Types.TINYINT) || 
        (sqltype == Types.INTEGER) ||            
        (sqltype == Types.SMALLINT) ||
        (sqltype == Types.BIGINT)    
       ;        
    }
    
    public static boolean isFixedNumeric(int sqltype) {
        return 
        (sqltype == Types.NUMERIC) || 
        (sqltype == Types.DECIMAL)    
       ;        
    }    
    
    public static boolean isFloatingPoint(int sqltype) {
        return 
        (sqltype == Types.REAL) || 
        (sqltype == Types.FLOAT) ||
        (sqltype == Types.DOUBLE)    
       ;        
    }        
}

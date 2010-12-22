/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.types.Type;

public abstract class SQLType
    extends DataTypeDefinition {

    @Override
    public Element getName() {
        return getSQLTypeName();
    }
    
    public abstract SQLType.Name getSQLTypeName();
    
    
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
    
    
    enum Name
    	implements Element {
        
    CHAR(Type.CHAR, Keyword.CHARACTER),
    VARCHAR(Type.VARCHAR, Keyword.VARCHAR),
    CLOB(Type.CLOB, Keyword.CLOB),
    BIGINT(Type.BIGINT, Keyword.BIGINT),
    BIT(Type.BIT, Keyword.BIT),    
    // BIT_VARYING(Type.BITV, Keyword.BIT, Keyword.VARYING),
    BLOB(Type.BLOB, Keyword.BLOB),
    NUMERIC(Type.NUMERIC, Keyword.NUMERIC), 
    DECIMAL(Type.DECIMAL, Keyword.DECIMAL),
    INTEGER(Type.INTEGER, Keyword.INTEGER),
    // INT(Type.INTEGER, Keyword.INT),
    SMALLINT(Type.SMALLINT, Keyword.SMALLINT),
    TINYINT(Type.TINYINT, Keyword.TINYINT),
    FLOAT(Type.FLOAT, Keyword.FLOAT),
    DATE(Type.DATE, Keyword.DATE),
    TIME(Type.TIME, Keyword.TIME),
    TIMESTAMP(Type.TIMESTAMP, Keyword.TIMESTAMP),
    ;
                
    private Name(int type, Keyword... kws) {
        this.type = type;
        this.keywords = kws;
    }
    
    private int type; 
    
    private Keyword[] keywords;
    
    @Override
    public String getTerminalSymbol() {     
        return null;
    }

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        for (Keyword kw : keywords) {
            kw.traverse(vc, v);
        }
    }
    
    public int getType() {
        return this.type;
    }
        
}    
}

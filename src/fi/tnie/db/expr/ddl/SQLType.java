/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.types.PrimitiveType;

public abstract class SQLType
    extends DataTypeDefinition {

    @Override
    public Element getName() {
        return getSQLTypeName();
    }
    
    public abstract SQLType.Name getSQLTypeName();
    
    
    public static boolean isTextType(int sqltype) {
        return 
            (sqltype == PrimitiveType.CHAR) || 
            (sqltype == PrimitiveType.VARCHAR) ||            
            (sqltype == PrimitiveType.LONGVARCHAR) ||
            (sqltype == PrimitiveType.NCHAR) ||
            (sqltype == PrimitiveType.NVARCHAR) ||
            (sqltype == PrimitiveType.LONGNVARCHAR) ||            
            (sqltype == PrimitiveType.CLOB)
           ;        
    }
    
    public static boolean isIntegralType(int sqltype) {
        return 
        (sqltype == PrimitiveType.TINYINT) || 
        (sqltype == PrimitiveType.INTEGER) ||            
        (sqltype == PrimitiveType.SMALLINT) ||
        (sqltype == PrimitiveType.BIGINT)    
       ;        
    }
    
    public static boolean isFixedNumeric(int sqltype) {
        return 
        (sqltype == PrimitiveType.NUMERIC) || 
        (sqltype == PrimitiveType.DECIMAL)    
       ;        
    }    
    
    public static boolean isFloatingPoint(int sqltype) {
        return 
        (sqltype == PrimitiveType.REAL) || 
        (sqltype == PrimitiveType.FLOAT) ||
        (sqltype == PrimitiveType.DOUBLE)    
       ;        
    }        
    
    
    enum Name
    	implements Element {
        
    CHAR(PrimitiveType.CHAR, Keyword.CHARACTER),
    VARCHAR(PrimitiveType.VARCHAR, Keyword.VARCHAR),
    CLOB(PrimitiveType.CLOB, Keyword.CLOB),
    BIGINT(PrimitiveType.BIGINT, Keyword.BIGINT),
    BIT(PrimitiveType.BIT, Keyword.BIT),    
    // BIT_VARYING(Type.BITV, Keyword.BIT, Keyword.VARYING),
    BLOB(PrimitiveType.BLOB, Keyword.BLOB),
    NUMERIC(PrimitiveType.NUMERIC, Keyword.NUMERIC), 
    DECIMAL(PrimitiveType.DECIMAL, Keyword.DECIMAL),
    INTEGER(PrimitiveType.INTEGER, Keyword.INTEGER),
    // INT(Type.INTEGER, Keyword.INT),
    SMALLINT(PrimitiveType.SMALLINT, Keyword.SMALLINT),
    TINYINT(PrimitiveType.TINYINT, Keyword.TINYINT),
    FLOAT(PrimitiveType.FLOAT, Keyword.FLOAT),
    DATE(PrimitiveType.DATE, Keyword.DATE),
    TIME(PrimitiveType.TIME, Keyword.TIME),
    TIMESTAMP(PrimitiveType.TIMESTAMP, Keyword.TIMESTAMP),
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

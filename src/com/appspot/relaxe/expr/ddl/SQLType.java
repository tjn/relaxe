/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.types.AbstractPrimitiveType;

public abstract class SQLType
    extends DataTypeDefinition {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8800518543021419377L;


	@Override
    public Element getName() {
        return getSQLTypeName();
    }
    
    public abstract SQLType.Name getSQLTypeName();
    
    
    public static boolean isTextType(int sqltype) {
        return 
            (sqltype == AbstractPrimitiveType.CHAR) || 
            (sqltype == AbstractPrimitiveType.VARCHAR) ||            
            (sqltype == AbstractPrimitiveType.LONGVARCHAR) ||
            (sqltype == AbstractPrimitiveType.NCHAR) ||
            (sqltype == AbstractPrimitiveType.NVARCHAR) ||
            (sqltype == AbstractPrimitiveType.LONGNVARCHAR) ||            
            (sqltype == AbstractPrimitiveType.CLOB)
           ;        
    }
    
    public static boolean isIntegralType(int sqltype) {
        return 
        (sqltype == AbstractPrimitiveType.TINYINT) || 
        (sqltype == AbstractPrimitiveType.INTEGER) ||            
        (sqltype == AbstractPrimitiveType.SMALLINT) ||
        (sqltype == AbstractPrimitiveType.BIGINT)    
       ;        
    }
    
    public static boolean isFixedNumeric(int sqltype) {
        return 
        (sqltype == AbstractPrimitiveType.NUMERIC) || 
        (sqltype == AbstractPrimitiveType.DECIMAL)    
       ;        
    }    
    
    public static boolean isFloatingPoint(int sqltype) {
        return 
        (sqltype == AbstractPrimitiveType.REAL) || 
        (sqltype == AbstractPrimitiveType.FLOAT) ||
        (sqltype == AbstractPrimitiveType.DOUBLE)    
       ;        
    }        
    
    
    enum Name
    	implements Element {
        
    CHAR(AbstractPrimitiveType.CHAR, SQLKeyword.CHARACTER),
    VARCHAR(AbstractPrimitiveType.VARCHAR, SQLKeyword.VARCHAR),
    CLOB(AbstractPrimitiveType.CLOB, SQLKeyword.CLOB),
    BIGINT(AbstractPrimitiveType.BIGINT, SQLKeyword.BIGINT),
    BIT(AbstractPrimitiveType.BIT, SQLKeyword.BIT),    
    // BIT_VARYING(AbstractType.BITV, Keyword.BIT, Keyword.VARYING),
    BLOB(AbstractPrimitiveType.BLOB, SQLKeyword.BLOB),
    NUMERIC(AbstractPrimitiveType.NUMERIC, SQLKeyword.NUMERIC), 
    DECIMAL(AbstractPrimitiveType.DECIMAL, SQLKeyword.DECIMAL),
    INTEGER(AbstractPrimitiveType.INTEGER, SQLKeyword.INTEGER),
    // INT(AbstractType.INTEGER, Keyword.INT),
    SMALLINT(AbstractPrimitiveType.SMALLINT, SQLKeyword.SMALLINT),
    TINYINT(AbstractPrimitiveType.TINYINT, SQLKeyword.TINYINT),
    FLOAT(AbstractPrimitiveType.FLOAT, SQLKeyword.FLOAT),
    DATE(AbstractPrimitiveType.DATE, SQLKeyword.DATE),
    TIME(AbstractPrimitiveType.TIME, SQLKeyword.TIME),
    TIMESTAMP(AbstractPrimitiveType.TIMESTAMP, SQLKeyword.TIMESTAMP),
    ;
                
    private Name(int type, SQLKeyword... kws) {
        this.type = type;
        this.keywords = kws;
    }
    
    private int type; 
    
    private SQLKeyword[] keywords;
    
    @Override
    public String getTerminalSymbol() {     
        return null;
    }

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        for (SQLKeyword kw : keywords) {
            kw.traverse(vc, v);
        }
    }
    
    public int getType() {
        return this.type;
    }
        
}    
}

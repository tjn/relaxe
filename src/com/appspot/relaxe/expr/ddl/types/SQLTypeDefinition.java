/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.TypeDefinition;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.types.PrimitiveType;

public abstract class SQLTypeDefinition
    extends DataTypeDefinition
    implements TypeDefinition {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8800518543021419377L;


	@Override
    public Element getTypeName() {
        return getSQLTypeName();
    }
    
    public abstract SQLTypeDefinition.Name getSQLTypeName();
    
    
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
    
    public static boolean isBinaryType(int sqltype) {
        return 
        (sqltype == PrimitiveType.BINARY) || 
        (sqltype == PrimitiveType.VARBINARY) ||
        (sqltype == PrimitiveType.LONGVARBINARY)
       ;            	
    }
    
    public static boolean isFloatingPoint(int sqltype) {
        return 
        (sqltype == PrimitiveType.REAL) || 
        (sqltype == PrimitiveType.FLOAT) ||
        (sqltype == PrimitiveType.DOUBLE)    
       ;        
    }
        
    
    
    public enum Name
    	implements Element {
        
	    CHAR(PrimitiveType.CHAR, SQLKeyword.CHARACTER),
	    VARCHAR(PrimitiveType.VARCHAR, SQLKeyword.VARCHAR),
	    LONGVARCHAR(PrimitiveType.LONGVARCHAR, SQLKeyword.VARCHAR),
	    CLOB(PrimitiveType.CLOB, SQLKeyword.CLOB),
	    BIGINT(PrimitiveType.BIGINT, SQLKeyword.BIGINT),
	    BIT(PrimitiveType.BIT, SQLKeyword.BIT),    
	    // BIT_VARYING(AbstractType.BITV, Keyword.BIT, Keyword.VARYING),
	    BLOB(PrimitiveType.BLOB, SQLKeyword.BLOB),
	    NUMERIC(PrimitiveType.NUMERIC, SQLKeyword.NUMERIC), 
	    DECIMAL(PrimitiveType.DECIMAL, SQLKeyword.DECIMAL),
	    INTEGER(PrimitiveType.INTEGER, SQLKeyword.INTEGER),
	    // INT(AbstractType.INTEGER, Keyword.INT),
	    SMALLINT(PrimitiveType.SMALLINT, SQLKeyword.SMALLINT),
	    TINYINT(PrimitiveType.TINYINT, SQLKeyword.TINYINT),
	    FLOAT(PrimitiveType.FLOAT, SQLKeyword.FLOAT),
	    DOUBLE(PrimitiveType.DOUBLE, null), // double precision
	    DATE(PrimitiveType.DATE, SQLKeyword.DATE),
	    TIME(PrimitiveType.TIME, SQLKeyword.TIME),
	    TIMESTAMP(PrimitiveType.TIMESTAMP, SQLKeyword.TIMESTAMP),	    
	    ARRAY(PrimitiveType.ARRAY, null),
	    VARBINARY(PrimitiveType.VARBINARY, SQLKeyword.VARBINARY),
	    ;
                
	    private Name(int type, SQLKeyword kws) {
	        this.type = type;
	        this.keyword = kws;
	    }
	    
	    private int type; 
	    
	    private SQLKeyword keyword;
	    
	    @Override
	    public String getTerminalSymbol() {     
	        return null;
	    }
	
	    @Override
	    public void traverse(VisitContext vc, ElementVisitor v) {
	    	v.start(vc, this);
	    	
	    	if (this.keyword == null) {
	    		throw new NullPointerException("keyword: " + toString());
	    	}
	    	
	    	this.keyword.traverse(vc, v);	        
	        v.end(this);
	    }
	    
	    public int getType() {
	        return this.type;
	    }        
	}    
}
/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.TypeDefinition;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.types.ValueType;

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
            (sqltype == ValueType.CHAR) || 
            (sqltype == ValueType.VARCHAR) ||            
            (sqltype == ValueType.LONGVARCHAR) ||
            (sqltype == ValueType.NCHAR) ||
            (sqltype == ValueType.NVARCHAR) ||
            (sqltype == ValueType.LONGNVARCHAR) ||            
            (sqltype == ValueType.CLOB)
           ;        
    }
    
    public static boolean isIntegralType(int sqltype) {
        return 
        (sqltype == ValueType.TINYINT) || 
        (sqltype == ValueType.INTEGER) ||            
        (sqltype == ValueType.SMALLINT) ||
        (sqltype == ValueType.BIGINT)    
       ;        
    }
    
    public static boolean isFixedNumeric(int sqltype) {
        return 
        (sqltype == ValueType.NUMERIC) || 
        (sqltype == ValueType.DECIMAL)    
       ;        
    }    
    
    public static boolean isBinaryType(int sqltype) {
        return 
        (sqltype == ValueType.BINARY) || 
        (sqltype == ValueType.VARBINARY) ||
        (sqltype == ValueType.LONGVARBINARY)
       ;            	
    }
    
    public static boolean isFloatingPoint(int sqltype) {
        return 
        (sqltype == ValueType.REAL) || 
        (sqltype == ValueType.FLOAT) ||
        (sqltype == ValueType.DOUBLE)    
       ;        
    }
        
    
    
    public enum Name
    	implements Element {
        
	    CHAR(ValueType.CHAR, SQLKeyword.CHARACTER),
	    VARCHAR(ValueType.VARCHAR, SQLKeyword.VARCHAR),
	    LONGVARCHAR(ValueType.LONGVARCHAR, SQLKeyword.VARCHAR),
	    CLOB(ValueType.CLOB, SQLKeyword.CLOB),
	    BIGINT(ValueType.BIGINT, SQLKeyword.BIGINT),
	    BIT(ValueType.BIT, SQLKeyword.BIT),    
	    // BIT_VARYING(AbstractType.BITV, Keyword.BIT, Keyword.VARYING),
	    BLOB(ValueType.BLOB, SQLKeyword.BLOB),
	    NUMERIC(ValueType.NUMERIC, SQLKeyword.NUMERIC), 
	    DECIMAL(ValueType.DECIMAL, SQLKeyword.DECIMAL),
	    INTEGER(ValueType.INTEGER, SQLKeyword.INTEGER),
	    // INT(AbstractType.INTEGER, Keyword.INT),
	    SMALLINT(ValueType.SMALLINT, SQLKeyword.SMALLINT),
	    TINYINT(ValueType.TINYINT, SQLKeyword.TINYINT),
	    FLOAT(ValueType.FLOAT, SQLKeyword.FLOAT),
	    DOUBLE(ValueType.DOUBLE, null), // double precision
	    DATE(ValueType.DATE, SQLKeyword.DATE),
	    TIME(ValueType.TIME, SQLKeyword.TIME),
	    TIMESTAMP(ValueType.TIMESTAMP, SQLKeyword.TIMESTAMP),	    
	    ARRAY(ValueType.ARRAY, null),
	    VARBINARY(ValueType.VARBINARY, SQLKeyword.VARBINARY),
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

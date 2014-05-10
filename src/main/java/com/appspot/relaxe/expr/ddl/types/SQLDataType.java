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


import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.Type;
import com.appspot.relaxe.types.ValueType;


/**
 * Syntactical element of SQL representing either predefined data type or user defined type. 
 *
 * @author tnie
 *
 */
public abstract class SQLDataType
    extends CompoundElement 
    implements Type {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3088251460191866613L;

	protected SQLDataType() {		
	}
	
    
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
    
    public static boolean isBinaryIntegerType(int sqltype) {
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
    
    public static boolean isApproximateNumber(int sqltype) {
        return 
        (sqltype == ValueType.REAL) || 
        (sqltype == ValueType.FLOAT) ||
        (sqltype == ValueType.DOUBLE)    
       ;        
    }    
}

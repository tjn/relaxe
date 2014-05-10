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

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public class SQLFloatType
    extends ApproximateNumericType {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -269346141333155275L;

	private static final SQLFloatType TYPE = new SQLFloatType();
	
	private Integer precision;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	private SQLFloatType() {		
	}
	
	public static SQLFloatType get() {
		return TYPE;
	}
	

    protected SQLFloatType(Integer precision) {
        this.precision = precision;
    }
        
    public Integer getPrecision() {
		return precision;
	}
            
    @Override
    public void traverseContent(VisitContext vc, ElementVisitor v) {
    	traverseName(vc, v);
    	
    	Integer p = getPrecision();
    	IntLiteral pe = (p == null) ? null : IntLiteral.valueOf(p.intValue());
    	
    	if (pe != null) {
    		Symbol.PAREN_LEFT.traverse(vc, v);
    		pe.traverse(vc, v);
    		Symbol.PAREN_RIGHT.traverse(vc, v);
    	}    	
    }	
    
	
	@Override
	protected void traverseName(VisitContext vc, ElementVisitor v) {
		SQLKeyword.FLOAT.traverse(vc, v);
	}
	
	public static SQLFloatType get(Integer precision) {		
		return (precision == null) ? SQLFloatType.get() : new SQLFloatType(precision);
	}	
	
}

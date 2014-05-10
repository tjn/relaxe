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
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public abstract class AbstractFixedPrecisionType
    extends AbstractNumericType {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 2301149943314207588L;
	
	private IntLiteral precision = null;
	private IntLiteral scale = null;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public AbstractFixedPrecisionType() {		
	}
	
	public AbstractFixedPrecisionType(int precision) {
		this.precision = IntLiteral.valueOf(precision);		 
	}
	    
    public AbstractFixedPrecisionType(int precision, int scale) {
    	this(Integer.valueOf(precision), Integer.valueOf(scale));
    }
    
    protected AbstractFixedPrecisionType(Integer precision, Integer scale) {
    	if (precision != null) {    		
        	this.precision = IntLiteral.valueOf(precision);
        	
        	if (precision.intValue() < 1) {
        		throw new IllegalArgumentException(
        				" precision (" + precision + ") must be > 0");        		
        	}
        	
        	
        	if (scale != null) {
        		if (scale < 0 || scale > precision) {
            		throw new IllegalArgumentException(
            				" scale (" + scale + ") must be between 0 and precision " + precision);
            	}
        		
        		this.scale = IntLiteral.valueOf(scale);	
        	}   		
    	}
    }
    
	@Override
	public final boolean isExact() {
		return true;
	}
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		traverseName(vc, v);
		
		if (this.precision != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);
			this.precision.traverse(vc, v);
			
			if (this.scale != null) {
				Symbol.COMMA.traverse(vc, v);
				this.scale.traverse(vc, v);
			}
			
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
	}
	
	protected abstract void traverseName(VisitContext vc, ElementVisitor v);
	
}

/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

public abstract class AbstractIntegalNumberDefinition
    extends AbstractNumericTypeDefinition {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2776206428444898457L;
	private Integer precision;

    protected AbstractIntegalNumberDefinition() {      
        this(null);
    }
    
    protected AbstractIntegalNumberDefinition(Integer precision) {
        this.precision = precision;
    }
    
    @Override
    public boolean isExact() {     
        return true;
    }
    
    public Integer getPrecision() {
		return precision;
	}
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
    	v.start(vc, this);
    	getSQLTypeName().traverse(vc, v);
    	
    	Integer p = getPrecision();
    	IntLiteral pe = (p == null) ? null : IntLiteral.valueOf(p.intValue());
    	
    	if (pe != null) {
    		Symbol.PAREN_LEFT.traverse(vc, v);
    		pe.traverse(vc, v);    		
    		Symbol.PAREN_RIGHT.traverse(vc, v);
    	}    	
    	
    	v.end(this);    	
    }
}

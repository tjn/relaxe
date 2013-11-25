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
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public class SQLArrayTypeDefinition
    extends SQLTypeDefinition {
       	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	private SQLTypeDefinition elementType;


	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private SQLArrayTypeDefinition() {
	}
	
	public SQLArrayTypeDefinition(SQLTypeDefinition elementType) {
		this.elementType = elementType;
	}
	
	@Override
	public Name getSQLTypeName() {
		return SQLTypeDefinition.Name.ARRAY;
	}
	

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
    	v.start(vc, this);
    	elementType.traverse(vc, v);
    	Symbol.BRACKET_LEFT.traverse(vc, v);
    	Symbol.BRACKET_RIGHT.traverse(vc, v);    	
    	v.end(this);
    }
    
    public SQLTypeDefinition getElementType() {
		return elementType;
	}
}

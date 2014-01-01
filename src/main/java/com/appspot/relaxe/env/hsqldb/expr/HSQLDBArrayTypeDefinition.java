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
package com.appspot.relaxe.env.hsqldb.expr;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;

public class HSQLDBArrayTypeDefinition
    extends SQLTypeDefinition {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3676886379250567100L;
	
	private SQLTypeDefinition elementType;
	private IntLiteral size;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private HSQLDBArrayTypeDefinition() {
	}
	
    public HSQLDBArrayTypeDefinition(SQLTypeDefinition elementType, Integer size) {
		super();
		this.elementType = elementType;
		this.size = (size == null) ? null : IntLiteral.valueOf(size.intValue());
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
        v.start(vc, this);
    
        this.elementType.traverse(vc, v);
        HSQLDBKeyword.ARRAY.traverse(vc, v);
        
        if (size != null) {
        	Symbol.BRACKET_LEFT.traverse(vc, v);
        	this.size.traverse(vc, v);
        	Symbol.BRACKET_RIGHT.traverse(vc, v);
        }        
        
        v.end(this);        
    }

	@Override
	public Name getSQLTypeName() {
		return null;
	}
}

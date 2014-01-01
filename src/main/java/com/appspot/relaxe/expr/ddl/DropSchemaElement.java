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
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.VisitContext;

public abstract class DropSchemaElement 
    extends SQLSchemaStatement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4367840517906725303L;
	private SQLKeyword elementType;
    private SQLKeyword cascade;
    private SchemaElementName name;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected DropSchemaElement() {
	}
    
    protected DropSchemaElement(Name statementName, SQLKeyword elementType, SchemaElementName name, Boolean cascade) {
        super(statementName);
        
        if (name == null) {
            throw new NullPointerException("'name' must not be null");
        }
        
        if (elementType == null) {
            throw new NullPointerException("'elementType' must not be null");
        }
        
        this.name = relativize(name);        
        this.elementType = elementType;
        this.cascade = cascade(cascade);
    }    
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        SQLKeyword.DROP.traverse(vc, v);        
        this.elementType.traverse(vc, v);                
        this.name.traverse(vc, v);
        traverseNonEmpty(this.cascade, vc, v);
    }
}
    
   

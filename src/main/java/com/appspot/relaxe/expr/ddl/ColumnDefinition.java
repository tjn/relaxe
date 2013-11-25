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
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.VisitContext;

public class ColumnDefinition
    extends CompoundElement
    implements BaseTableElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1225762403113588730L;
	private Identifier name;
    private ColumnDataType dataType;
    private ElementList<ColumnConstraint> constraintList;    
    private DefaultDefinition defaultDefinition;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected ColumnDefinition() {
	}
    
    public ColumnDefinition(Identifier name, ColumnDataType type) {
        this(name, type, null, null);
    }
    
    public ColumnDefinition(Identifier name, ColumnDataType type, DefaultDefinition defaultDefinition, ElementList<ColumnConstraint> constraintList) {
        super();
        
        if (name == null) {
            throw new NullPointerException("'name' must not be null");
        }
        
        if (type == null) {
            throw new NullPointerException("'dataType' must not be null");
        }
        
        this.name = name;
        this.dataType = type;
        this.defaultDefinition = defaultDefinition;
        this.constraintList = constraintList;
    }
    
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        this.name.traverse(vc, v);
        this.dataType.traverse(vc, v);
        
        traverseNonEmpty(this.defaultDefinition, vc, v);
        traverseNonEmpty(this.constraintList, vc, v);                
    }

    public ElementList<ColumnConstraint> getConstraintList() {
        return constraintList;
    }    

}

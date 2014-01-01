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
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class DoubleTypeDefinition
    extends FloatingPointDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -269346141333155275L;

	public static final DoubleTypeDefinition DEFINITION = new DoubleTypeDefinition();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	private DoubleTypeDefinition() {		
	}
	
    @Override
    public SQLTypeDefinition.Name getSQLTypeName() {
    	return SQLTypeDefinition.Name.DOUBLE;
    }
            
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
    	v.start(vc, this);
    	SQLKeyword.DOUBLE.traverse(vc, v);
    	SQLKeyword.PRECISION.traverse(vc, v);
    	v.end(this);
    }
}

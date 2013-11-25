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
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.ColumnDataType;

/**
 * Simple type name.
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public class TypeDefinition
	extends SQLTypeDefinition
	implements ColumnDataType {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6588251307620934502L;
	
	private SchemaElementName name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected TypeDefinition() {
	}

	public TypeDefinition(SchemaElementName name) {
		super();
		
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		this.name.traverse(vc, v);
		v.end(this);
	}

	@Override
	public SQLTypeDefinition.Name getSQLTypeName() {
		return null;
	}
}

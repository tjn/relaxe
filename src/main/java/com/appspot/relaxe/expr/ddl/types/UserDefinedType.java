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

import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.VisitContext;

/**
 * Use defined type
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public class UserDefinedType
	extends SQLDataType {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6588251307620934502L;
	
	private Element name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected UserDefinedType() {
	}
	
	private UserDefinedType(Element name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;		
	}
	
	public UserDefinedType(SchemaElementName name) {
		this((Element) name);		
	}

	public UserDefinedType(Identifier name) {
		this((Element) name);
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		this.name.traverse(vc, v);
		v.end(this);
	}
}

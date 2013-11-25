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
package com.appspot.relaxe.expr;

public class Assignment extends CompoundElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7845480008197565746L;
	private Identifier name;
	private Element value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected Assignment() {
	}
	
	/**
	 * Constructs an assignment using the <code>value</code> as an expression or SQL NULL value if the <code>value</code> 
	 * is null. 
	 * 
	 * <p>	
	 * If <code>value</code> is not <code>null</code>:
	 * </p>
	 * 
	 * <p><sql> 
	 *  UPDATE T SET C = &lt;value&gt;
	 * </sql></p>
	 * 
	 * <p>If <code>value</code> is <code>null</code>:</p>
	 * <p><sql>
	 *  UPDATE T SET C = NULL 
	 * </sql></p>
	 * 
	 *  
	 * @param name
	 */	

	public Assignment(Identifier name, ValueExpression value) {
		this(name, (Element) value);		
	}
	
	/**
	 * <p>Constructs an assignment using the SQL DEFAULT -value.</p>
	 * 
	 * <psql><sql>
	 *  UPDATE UPDATE_EXAMPLE SET NAME = DEFAULT 
	 * </sql></psql>
	 *  
	 * @param name
	 */	
	public Assignment(Identifier name) {
		this(name, SQLKeyword.DEFAULT);
	}
	
	private Assignment(Identifier name, Element value) {
		super();
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		this.name = name;		
		this.value = (value == null) ? SQLKeyword.NULL : value;
	}		
		
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		name.traverse(vc, v);
		Symbol.EQUALS.traverse(vc, v);
		value.traverse(vc, v);
		v.end(this);
	}
}

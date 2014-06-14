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
package com.appspot.relaxe.expr;


/**
 * Represents the SQL DEFAULT -value expression in 
 * VALUES -clause of the INSERT -statement and the right-hand side of the assignment in UPDATE -statement.
 * 
 * <sql>
 * 	INSERT INTO DEFAULT_TEST VALUES (DEFAULT, DEFAULT)
 * </sql>
 * 
 *<sql>
 * 	UPDATE DEFAULT_TEST SET NAME = DEFAULT
 * </sql>
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public class Default
	extends CompoundElement
	implements RowValueConstructorElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1791929423714072637L;

	/**
	 * No-argument constructor for GWT Serialization
	 */		
	public Default() {		
	}

	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.DEFAULT.traverse(vc, v);
	}
	

	@Override
	public ValueExpression asValueExpression() {
		return null;
	}

	@Override
	public Element asNullSpecification() {
		return null;
	}

	@Override
	public Element asDefaultSpecification() {
		return this;
	}
}	

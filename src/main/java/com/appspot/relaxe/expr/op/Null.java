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
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.RowValueConstructorElement;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;

/**
 * Represents the SQL NULL expression in 
 * VALUES -clause of the INSERT -statement and the right-hand side of the assignment in UPDATE -statement.
 * 
 * <sql>
 * 	INSERT INTO T VALUES (NULL, NULL)
 * </sql>
 * 
 *<sql>
 * 	UPDATE T SET NAME = NULL
 * </sql>
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public class Null
	extends CompoundElement
	implements RowValueConstructorElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5908751449877328237L;

	/**
	 * No-argument constructor for GWT Serialization
	 */		
	public Null() {
	}

	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.NULL.traverse(vc, v);
	}		

	@Override
	public ValueExpression asValueExpression() {
		return null;
	}

	@Override
	public Element asNullSpecification() {
		return this;
	}

	@Override
	public Element asDefaultSpecification() {
		return null;
	}
}
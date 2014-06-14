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
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.RowValueConstructor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;


/**
 * 
 */
public class AbstractInPredicate<I extends Element>
	extends CompoundElement
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4384506983416435234L;
	private RowValueConstructor value;
	private I in;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractInPredicate() {
	}

	protected AbstractInPredicate(RowValueConstructor value, I in) {
		super();
		this.value = value;				
		this.in = in;				
	}

//	
//	public In(RowValueConstructor left, Collection<RowValueConstructor> values) {
//		this.left = left;				
//		this.value = new ElementList<RowValueConstructor>(Symbol.COMMA, values);
//	}
//	
//	public In(ValueExpression left, QueryExpression subquery) {
//		super();
//		this.left = left;
//		this.value = subquery;
//	}
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		this.value.traverse(vc, v);				
		SQLKeyword.IN.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.in.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);		
	}
		
	public RowValueConstructor getValue() {
		return value;
	}
	
	public I getIn() {
		return in;
	}
}

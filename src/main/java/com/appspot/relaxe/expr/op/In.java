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

import java.util.Collection;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;


/**
 * 
 */
public class In
	extends CompoundElement
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4384506983416435234L;
	private ValueExpression left;
	private ElementList<ValueExpression> elements;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected In() {
	}
	
	public In(ValueExpression left, Collection<ValueExpression> values) {
		super();
		this.left = left;				
		this.elements = new ElementList<ValueExpression>(Symbol.COMMA, values);				
	}
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		left.traverse(vc, v);				
		SQLKeyword.IN.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.elements.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);		
	}
	
}

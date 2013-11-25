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
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Expression;
import com.appspot.relaxe.expr.Token;
import com.appspot.relaxe.expr.VisitContext;

public class BinaryOperator implements Expression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7598734497611789621L;
	private Token symbol;	
	private Expression left;
	private Expression right;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected BinaryOperator() {
	}
	
	public BinaryOperator(Token symbol, Expression left, Expression right) {
		super();
		
		if (symbol == null) {
			throw new NullPointerException("'symbol' must not be null");
		}
		
		if (left == null) {
			throw new NullPointerException("'left' must not be null");
		}
		
		if (right == null) {
			throw new NullPointerException("'right' must not be null");
		}
		
		this.symbol = symbol;
		this.left = left;
		this.right = right;				
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		left.traverse(vc, v);
		symbol.traverse(vc, v);		
		right.traverse(vc, v);
		v.end(this);
	}

	@Override
	public String getTerminalSymbol() {
		return null;
	}
}

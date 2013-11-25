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
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class IntLiteral	
	implements Token, ValueExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349786152502099361L;
	
	private int value;
	
	
	public static final IntLiteral ZERO = new IntLiteral(0);
	public static final IntLiteral ONE = new IntLiteral(1); 
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public IntLiteral() {
		this(0);
	}
	
	public IntLiteral(int value) {
		this.value = value;
	}
	
	public static IntLiteral valueOf(int value) {
		switch (value) {
		case 0:
			return ZERO;
		case 1:
			return ONE;
		default:			
		}
		
		return new IntLiteral(value);
	}

	@Override
	public String getTerminalSymbol() {
		return Integer.toString(this.value);
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {		
		v.start(vc, (Token) this);
		v.end(this);			
	}
	
	@Override
	public int getType() {
		return PrimitiveType.INTEGER;
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
	
	@Override
	public Identifier getColumnName() {
		return null;
	}
}
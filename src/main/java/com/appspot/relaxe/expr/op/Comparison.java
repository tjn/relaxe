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

import com.appspot.relaxe.expr.Keyword;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.RowValueConstructor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.Token;
import com.appspot.relaxe.expr.ValueExpression;

public class Comparison
	extends BinaryOperator
	implements Predicate {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6172456596029288843L;

	public enum Op {
		EQ(Symbol.EQUALS),
		LE(Symbol.LESS_OR_EQUAL),
		GE(Symbol.GREATER_OR_EQUAL),
		LT(Symbol.LESS_THAN),
		GT(Symbol.GREATER_THAN),
		LIKE(SQLKeyword.LIKE),
		;
				
		private Token symbol;
		
		private Op() {			
		}
		
		private Op(Symbol symbol) {
			this.symbol = symbol;			
		}
		
		private Op(Keyword keyword) {
			this.symbol = keyword;			
		}
		
		public Comparison newComparison(ValueExpression a, ValueExpression b) {
			return new Comparison(this, a, b);
		}
		
		public Comparison newComparison(RowValueConstructor a, RowValueConstructor b) {
			return new Comparison(this, a, b);
		}
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Comparison() {
	}
	
	private Comparison(Op op, ValueExpression a, ValueExpression b) {
		super(op.symbol, a, b);	
	}
	
	private Comparison(Op op, RowValueConstructor a, RowValueConstructor b) {
		super(op.symbol, a, b);	
	}		
	
	public static Comparison eq(ValueExpression a, ValueExpression b) {
		return new Comparison(Op.EQ, a, b);
	}
	
	public static Comparison lt(ValueExpression a, ValueExpression b) {
		return new Comparison(Op.LT, a, b);
	}	
	
	public static Comparison gt(ValueExpression a, ValueExpression b) {
		return new Comparison(Op.GT, a, b);
	}

	public static Comparison like(ValueExpression a, ValueExpression b) {
		return new Comparison(Op.LIKE, a, b);
	}
	
	public static Comparison eq(RowValueConstructor a, RowValueConstructor b) {
		return new Comparison(Op.EQ, a, b);
	}
		
}

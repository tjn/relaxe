/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.Symbol;
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
		;
				
		private Symbol symbol;
		
		private Op() {			
		}
		
		private Op(Symbol symbol) {
			this.symbol = symbol;			
		}
		
		public Comparison newComparison(ValueExpression a, ValueExpression b) {
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
	
	public static Comparison eq(ValueExpression a, ValueExpression b) {
		return new Comparison(Op.EQ, a, b);
	}
	
	public static Comparison lt(ValueExpression a, ValueExpression b) {
		return new Comparison(Op.LT, a, b);
	}	
	
	public static Comparison gt(ValueExpression a, ValueExpression b) {
		return new Comparison(Op.GT, a, b);
	}

	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}	
}

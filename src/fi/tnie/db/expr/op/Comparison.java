/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.ValueExpression;

public class Comparison
	extends BinaryOperator
	implements Predicate {
		
	public enum Op {
		EQ(Symbol.EQUALS),
		LE(Symbol.LESS_OR_EQUAL),
		GE(Symbol.GREATER_OR_EQUAL),
		LT(Symbol.LESS_THAN),
		GT(Symbol.GREATER_THAN),
		;
				
		private Symbol symbol;
		
		private Op(Symbol symbol) {
			this.symbol = symbol;			
		}
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
}

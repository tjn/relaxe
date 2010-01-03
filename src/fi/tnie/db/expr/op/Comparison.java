/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.VisitContext;

public class Comparison
	extends Operator
	implements Predicate {
		
	public enum Op
		implements Element {
		EQ("="),
		LE("<"),
		GE(">"),
		LT("<="),
		GT(">="),
		;
				
		private String symbol;
		
		private Op(String symbol) {
			this.symbol = symbol;			
		}
		
		@Override
		public String getTerminalSymbol() {
			return this.symbol;
		}

		@Override
		public void traverse(VisitContext vc, ElementVisitor v) {
			v.start(vc, this);
		}
	}
	
	private Comparison(Op op, ValueExpression a, ValueExpression b) {
		super(op, a, b);
		// TODO: check types
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

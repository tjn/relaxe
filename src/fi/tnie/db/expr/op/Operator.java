/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Expression;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.VisitContext;

public class Operator implements Expression {
	
	private Element symbol;	
	private Expression left;
	private Expression right;
	
	public Operator(Element symbol, Expression left, Expression right) {
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

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("(");
//		left.generate(qc, dest);
//		dest.append(this.symbol);
//		right.generate(qc, dest);
//		dest.append(") ");
//	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		
		Symbol.PAREN_LEFT.traverse(vc, v);
		left.traverse(vc, v);
		symbol.traverse(vc, v);		
		right.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);

		v.end(this);
	}

	@Override
	public String getTerminalSymbol() {
		return null;
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
		
//		Symbol.PAREN_LEFT.traverse(vc, v);
		left.traverse(vc, v);
		symbol.traverse(vc, v);		
		right.traverse(vc, v);
//		Symbol.PAREN_RIGHT.traverse(vc, v);

		v.end(this);
	}

	@Override
	public String getTerminalSymbol() {
		return null;
	}
}

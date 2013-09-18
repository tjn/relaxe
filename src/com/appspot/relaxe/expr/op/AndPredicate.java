/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;

public class AndPredicate
	extends BinaryOperator
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -13632007707506532L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AndPredicate() {
	}
	
	public AndPredicate(Predicate left, Predicate right) {
		super(SQLKeyword.AND, left, right);
	}
	
	public static Predicate newAnd(Predicate a, Predicate b) {
		if (a == null) {
			return b;
		}
		
		if (b == null) {
			return a;
		}
		
		return new AndPredicate(a, b);
	}
	
	
	public static Predicate newAnd(Predicate a, Predicate b, Predicate ... tail) {
		Predicate p = newAnd(a, b);
		
		for (int i = 0; i < tail.length; i++) {
			p = newAnd(p, tail[i]);
		}
		
		return p;
	}
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}

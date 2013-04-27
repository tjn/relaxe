/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;

public class OrPredicate
	extends BinaryOperator
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1087029444548580436L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected OrPredicate() {
	}
	
	public OrPredicate(Predicate left, Predicate right) {
		super(SQLKeyword.OR, left, right);
	}
	
	public static Predicate newOr(Predicate a, Predicate b) {
		if (a == null) {
			return b;
		}
		
		if (b == null) {
			return a;
		}
		
		return new OrPredicate(a, b);
	}
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}

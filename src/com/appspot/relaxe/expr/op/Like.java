/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.ValueExpression;

public class Like
	extends BinaryOperator
	implements Predicate {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6172456596029288843L;

	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Like() {
	}
	
	private Like(ValueExpression a, ValueExpression b) {
		super(SQLKeyword.LIKE, a, b);
	}	
	
	public static Like like(ValueExpression a, ValueExpression b) {
		return new Like(a, b);
	}

	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.Predicate;

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
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}

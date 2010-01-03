/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.Predicate;

public class AndPredicate
	extends BinaryOperator
	implements Predicate {
	
	public AndPredicate(Predicate left, Predicate right) {
		super(Keyword.AND, left, right);
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
}

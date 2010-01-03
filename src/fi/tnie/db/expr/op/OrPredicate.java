/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.Predicate;

public class OrPredicate
	extends BinaryOperator
	implements Predicate {
	
	public OrPredicate(Predicate left, Predicate right) {
		super(Keyword.OR, left, right);
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
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.ValueExpression;

public class Eq
	extends ValueOperator
	implements Predicate {

	public Eq(ValueExpression left, ValueExpression right) {
		super(Symbol.EQUALS, left, right);
	}		
}

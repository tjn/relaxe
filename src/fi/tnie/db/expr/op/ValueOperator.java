/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Token;
import fi.tnie.db.expr.ValueExpression;

public class ValueOperator
	extends BinaryOperator {

	public ValueOperator(Token symbol, ValueExpression left, ValueExpression right) {
		super(symbol, left, right);	
	}
}

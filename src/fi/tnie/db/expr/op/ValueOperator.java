/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ValueExpression;

public class ValueOperator
	extends Operator {

	public ValueOperator(Element symbol, ValueExpression left, ValueExpression right) {
		super(symbol, left, right);	
	}
}

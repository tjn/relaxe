/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Token;
import fi.tnie.db.expr.ValueExpression;

public class ValueOperator
	extends BinaryOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6471434380748487711L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ValueOperator() {
	}
	
	public ValueOperator(Token symbol, ValueExpression left, ValueExpression right) {
		super(symbol, left, right);	
	}
}

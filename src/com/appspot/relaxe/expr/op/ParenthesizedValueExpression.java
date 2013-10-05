/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ValueExpression;

public class ParenthesizedValueExpression
	extends Parenthesis<ValueExpression>
	implements ValueExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6979122703202314520L;


	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private ParenthesizedValueExpression() {
	}
	
	public ParenthesizedValueExpression(ValueExpression content) {
		super(content);		
	}

	@Override
	public int getType() {
		return getContent().getType();
	}

	@Override
	public Identifier getColumnName() {
		return getContent().getColumnName();
	}
}

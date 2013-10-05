/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public interface ValueExpression 
	extends Expression, ValuesListElement {
	/**
	 * SQL JavaType of the expression
	 * 
	 * @return
	 */
	int getType();
	
	/**
	 * The name of the column the expression originates from, if available.
	 *  
	 * @return
	 */
	Identifier getColumnName();
}

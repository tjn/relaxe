/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public interface QueryExpression
	extends Expression {

	public TableExpression getTableExpr();
	public OrderBy getOrderBy();
	public Limit getLimit();
	public Offset getOffset();

	String generate();

}
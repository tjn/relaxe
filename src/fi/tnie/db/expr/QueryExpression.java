/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public interface QueryExpression
	extends Expression {

	public abstract TableExpression getTableExpr();

	public abstract OrderBy getOrderBy();

	public abstract Limit getLimit();

	public abstract Offset getOffset();

	String generate();

}
/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

/**
 * Top-level SELECT -statement
 * @author Administrator
 */

public abstract class QueryExpression 	
	extends Statement {
	
	protected QueryExpression() {
		super(Name.SELECT);
	}	
	
	public abstract TableExpression getTableExpr();
	public abstract OrderBy getOrderBy();
	public abstract Limit getLimit();
	public abstract Offset getOffset();
//	public abstract Select getSelect();
}

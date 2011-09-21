/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.ent.QueryExpressionSource;

/**
 * Top-level SELECT -statement
 * @author Administrator
 */

public abstract class QueryExpression
	extends Statement
	implements QueryExpressionSource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5828745586955669361L;
	
	protected QueryExpression() {
		super(Name.SELECT);
	}	
	
	public abstract TableExpression getTableExpr();
	public abstract OrderBy getOrderBy();
	public abstract Limit getLimit();
	public abstract Offset getOffset();
//	public abstract Select getSelect();
	
	@Override
	public QueryExpression getQueryExpression() {
		return this;
	}
}

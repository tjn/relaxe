/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.ent.QueryExpressionSource;

/**
 * Top-level SELECT -statement
 * @author Administrator
 */

public abstract class AbstractQueryExpression
	extends CompoundElement
	implements QueryExpressionSource, QueryExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5828745586955669361L;
	
	protected AbstractQueryExpression() {
		super();
	}	
	
	/* (non-Javadoc)
	 * @see fi.tnie.db.expr.QueryExpression#getTableExpr()
	 */
	public abstract TableExpression getTableExpr();
	/* (non-Javadoc)
	 * @see fi.tnie.db.expr.QueryExpression#getOrderBy()
	 */
	public abstract OrderBy getOrderBy();
	/* (non-Javadoc)
	 * @see fi.tnie.db.expr.QueryExpression#getLimit()
	 */
	public abstract Limit getLimit();
	/* (non-Javadoc)
	 * @see fi.tnie.db.expr.QueryExpression#getOffset()
	 */
	public abstract Offset getOffset();
//	public abstract Select getSelect();
	
	@Override
	public QueryExpression getQueryExpression() {
		return this;
	}
}

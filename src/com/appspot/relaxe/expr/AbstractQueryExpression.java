/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.ent.QueryExpressionSource;

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
	 * @see com.appspot.relaxe.expr.QueryExpression#getTableExpr()
	 */
	@Override
	public abstract TableExpression getTableExpr();
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.expr.QueryExpression#getOrderBy()
	 */
	@Override
	public abstract OrderBy getOrderBy();
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.expr.QueryExpression#getLimit()
	 */
	@Override
	public abstract Limit getLimit();
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.expr.QueryExpression#getOffset()
	 */
	@Override
	public abstract Offset getOffset();
//	public abstract Select getSelect();
	
	@Override
	public QueryExpression getQueryExpression() {
		return this;
	}
}

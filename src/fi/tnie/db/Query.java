/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.expr.QueryExpression;

public class Query
	extends AbstractRequest {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1412811343522826948L;
	
	private QueryExpression expression;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Query() {
	}
	
	public Query(QueryExpression expression) {
		super();
		
		if (expression == null) {
			throw new NullPointerException("expression");
		}
		
		this.expression = expression;
	}

	public QueryExpression getExpression() {
		return expression;
	}
}

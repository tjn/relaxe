/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.query;

import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.rpc.AbstractRequest;

public class Query
	extends AbstractRequest {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1412811343522826948L;
	
	private transient QueryExpression expression;
	
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

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.query.QueryException;


public interface QueryExpressionSource
	extends Serializable {
	QueryExpression getQueryExpression()
		throws QueryException;
}

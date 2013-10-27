/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.service;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.query.QueryException;

public interface QuerySession {

	DataObjectQueryResult<DataObject> execute(QueryExpression qe, FetchOptions opts)
		throws QueryException;

}

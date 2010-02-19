/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.expr.QueryExpression;

public interface InstantiationContext<R extends Row> {
	
	public QueryExpression getQueryExpression();
	 
}

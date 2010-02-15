/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.expr.SelectQuery;

public interface InstantiationContext {
	
	public SelectQuery getQueryExpression();
}

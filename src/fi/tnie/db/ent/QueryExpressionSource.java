/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.QueryExpression;

public interface QueryExpressionSource {
	QueryExpression getQueryExpression()
		throws EntityException;
}

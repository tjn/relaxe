/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.query.QueryException;

public interface QueryExpressionSource
	extends Serializable {
	QueryExpression getQueryExpression()
		throws QueryException;
}

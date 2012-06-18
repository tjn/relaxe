/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.service;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.query.QueryException;

public interface QuerySession {

	DataObjectQueryResult<DataObject> executeQuery(QueryExpressionSource qes, FetchOptions opts)
		throws QueryException;

}

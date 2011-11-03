/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.QueryExpressionSource;

public interface DataObjectFetcher
	extends Fetcher<QueryExpressionSource, DataObjectQueryResult<DataObject>, Receiver<DataObjectQueryResult<DataObject>>> {
}

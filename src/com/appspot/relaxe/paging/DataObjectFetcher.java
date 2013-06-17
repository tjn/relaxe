/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.QueryExpressionSource;

public interface DataObjectFetcher
	extends Fetcher<QueryExpressionSource, DataObjectQueryResult<DataObject>, PageReceiver<DataObjectQueryResult<DataObject>>> {
}

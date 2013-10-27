/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.expr.QueryExpression;

public interface DataObjectFetcher
	extends Fetcher<QueryExpression, DataObjectQueryResult<DataObject>, PageReceiver<DataObjectQueryResult<DataObject>>> {
}

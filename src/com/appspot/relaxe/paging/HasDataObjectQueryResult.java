/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;

public interface HasDataObjectQueryResult<O extends DataObject> {
	DataObjectQueryResult<O> getResult();
}

package com.appspot.relaxe.service;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.expr.SelectStatement;

public interface QueryResultReceiver {

	void received(SelectStatement statement, DataObjectQueryResult<DataObject> result);
}

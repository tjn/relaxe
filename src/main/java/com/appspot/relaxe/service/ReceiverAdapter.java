package com.appspot.relaxe.service;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.Statement;

public class ReceiverAdapter
	implements Receiver {

	@Override
	public void received(SelectStatement statement, DataObjectQueryResult<DataObject> result) {
	}

	@Override
	public void updated(Statement statement, int updateCount) {
	}
}

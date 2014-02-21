package com.appspot.relaxe.service;

import com.appspot.relaxe.expr.Statement;

public interface UpdateReceiver {

	void updated(Statement statement, int updateCount);
	
}

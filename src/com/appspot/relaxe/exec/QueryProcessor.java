/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.exec;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.query.QueryException;


public interface QueryProcessor {

	public void prepare();
		
	public void updated(int updateCount) 
		throws SQLException;
	
	public void process(ResultSet rs, long ordinal) 
		throws QueryException, SQLException;
	
	public void abort(Throwable e);
	
	public void startQuery(ResultSetMetaData m) 
		throws QueryException, SQLException;
	
	public void endQuery()
		throws QueryException;

	public void finish();
}

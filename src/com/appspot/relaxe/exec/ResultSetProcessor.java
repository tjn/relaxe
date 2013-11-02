/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.exec;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.query.QueryException;

public interface ResultSetProcessor
	extends ResultProcessor
{
	public void process(ResultSet rs, long ordinal) 
		throws QueryException, SQLException;
	
	public void abort(Exception e)
		throws QueryException, SQLException;
	
	public void startResultSet(ResultSetMetaData m) 
		throws QueryException, SQLException;
	
	public void endResultSet()
		throws QueryException, SQLException;
}

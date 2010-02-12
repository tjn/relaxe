/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface QueryProcessor {

	public void prepare() 
		throws SQLException;
		
	public void updated(int updateCount) 
		throws SQLException;
	
	public void process(ResultSet rs, long ordinal) 
		throws SQLException;
	
	public void abort(Throwable e);
	
	public void startQuery(ResultSetMetaData m) 
		throws SQLException;
	
	public void endQuery()
		throws SQLException;

	public void finish();
}

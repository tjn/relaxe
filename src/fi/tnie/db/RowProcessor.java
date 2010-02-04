/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;

public interface RowProcessor {
	
	void prepare();	
	void finish();
	void abort(Throwable cause);
	
	void start(ResultSet rs);	
	void startRow(ResultSet rs);	
	void value(ResultSet rs);	
	void endRow(ResultSet rs);
	void end(ResultSet rs); 
}

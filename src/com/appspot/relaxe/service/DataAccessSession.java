/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.service;

public interface DataAccessSession {
	void commit()
		throws DataAccessException;

	void rollback()
		throws DataAccessException;
	
	void close()
		throws DataAccessException;	
	
	EntitySession asEntitySession();
	QuerySession asQuerySession();
	StatementSession asStatementSession();
}

package com.appspot.relaxe.service;

public interface TransactionControlSession {

	void commit()
		throws DataAccessException;

	void rollback()
		throws DataAccessException;
}

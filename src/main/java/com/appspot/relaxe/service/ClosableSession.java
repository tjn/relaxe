package com.appspot.relaxe.service;

public interface ClosableSession {

	void close()
		throws DataAccessException;	

	boolean isClosed();
}

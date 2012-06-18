/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.service;

public interface DataAccessContext {
	
	DataAccessSession newSession()
		throws DataAccessException;

	
}

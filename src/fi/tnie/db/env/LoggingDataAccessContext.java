/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import fi.tnie.db.log.Logger;
import fi.tnie.db.service.DataAccessContext;
import fi.tnie.db.service.DataAccessException;
import fi.tnie.db.service.DataAccessSession;

public class LoggingDataAccessContext
	implements DataAccessContext {
	
	private DataAccessContext inner;
	private Logger logger;
	
	public LoggingDataAccessContext(DataAccessContext inner, Logger logger) {
		super();
		this.inner = inner;
		this.logger = logger;
	}

	@Override
	public DataAccessSession newSession() throws DataAccessException {
		long start = System.currentTimeMillis();
		DataAccessSession das = inner.newSession();
		long elapsed = System.currentTimeMillis() - start;
		
		logger.debug("newSession(): " + elapsed + "ms"); 
		
		return das;
	}
	

}

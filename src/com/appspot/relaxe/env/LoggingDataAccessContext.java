/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import com.appspot.relaxe.log.Logger;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.DataAccessSession;

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

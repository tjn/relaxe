/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.log.Logger;

public class Log4JLogger
	implements Logger {
	
	private org.apache.log4j.Logger inner;
		
	public static Logger getLogger(Class<?> clazz) {
		return new Log4JLogger(org.apache.log4j.Logger.getLogger(clazz));
	}
	
	public Log4JLogger(org.apache.log4j.Logger inner) {
		super();
		this.inner = inner;
	}

	@Override
	public void debug(String msg) {
		inner.debug(msg);		
	}

	@Override
	public void error(String msg) {
		inner.error(msg);
		
	}

	@Override
	public void error(String msg, Throwable t) {
		inner.error(msg, t);		
	}

	@Override
	public void fatal(String msg) {
		inner.fatal(msg);
		
	}

	@Override
	public void info(String msg) {
		inner.info(msg);		
	}

	@Override
	public void warn(String msg) {
		inner.warn(msg);		
	}

	

}
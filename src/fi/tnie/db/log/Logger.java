/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.log;

public interface Logger {
	void debug(String msg);
	void info(String msg);
	void error(String msg);
	void error(String msg, Exception t);
	
	void fatal(String msg);
	
}

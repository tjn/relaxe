/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.log;

public interface Logger {
	void debug(String msg);
	void info(String msg);
	void warn(String msg);
	void error(String msg);
	void error(String msg, Throwable t);
	
	void fatal(String msg);
	
}

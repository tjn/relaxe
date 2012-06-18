/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.log;

import java.util.logging.Level;


public class JavaLogger
	implements Logger {
	
	private java.util.logging.Logger implementation;
		
	public static Logger getLogger(Class<?> clazz) {
		return new JavaLogger(java.util.logging.Logger.getLogger(clazz.getName()));
	}
	
	public JavaLogger(java.util.logging.Logger implementation) {
		super();
		
		if (implementation == null) {
			throw new NullPointerException("implementation");
		}
		
		this.implementation = implementation;
	}

	@Override
	public void debug(String msg) {
		implementation.fine(msg);
	}

	@Override
	public void error(String msg) {
		implementation.log(Level.SEVERE, msg);		
	}

	@Override
	public void error(String msg, Throwable t) {
		implementation.log(Level.SEVERE, msg, t);		
	}

	@Override
	public void fatal(String msg) {
		implementation.log(Level.SEVERE, msg);		
	}

	@Override
	public void info(String msg) {
		implementation.log(Level.INFO, msg);		
	}

	@Override
	public void warn(String msg) {
		implementation.log(Level.WARNING, msg);		
	}

}

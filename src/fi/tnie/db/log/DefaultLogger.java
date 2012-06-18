/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.log;


public class DefaultLogger
	implements Logger {
	
	private Logger target;

	private static DefaultLogger instance;
	
	public static DefaultLogger getInstance() {
		if (instance == null) {
			instance = new DefaultLogger();			
		}
	
		return instance;
	}
	
	public static Logger getLogger() {
		DefaultLogger li = getInstance();
		return (li.target == null) ? li : li.target;
	}

	@Override
	public void debug(String msg) {	
	}

	@Override
	public void error(String msg) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void error(String msg, Throwable t) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void info(String msg) {
		// TODO Auto-generated method stub		
	}

	public Logger getTarget() {
		return this.target;
	}


	public void setTarget(Logger target) {
		this.target = target;
	}

	@Override
	public void fatal(String msg) {
//		Window.alert("FATAL: " + msg);
	}

	@Override
	public void warn(String msg) {
		
		
	}
}

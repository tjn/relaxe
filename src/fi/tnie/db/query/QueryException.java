/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.query;

public class QueryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3983568740898490996L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected QueryException() {
	}

	public QueryException(String msg, Throwable cause) {
		super(msg, cause);	
	}
	
	public QueryException(String msg) {
		super(msg);
	}
	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.service;

public class DataAccessException
	extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6134393660701489647L;

	public DataAccessException() {
		super();
	}
	
	public DataAccessException(String msg) {
		super(msg);
	}

	public DataAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}	
}

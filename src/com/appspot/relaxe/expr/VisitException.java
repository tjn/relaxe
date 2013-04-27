/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class VisitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2802246489601806383L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected VisitException() {
	}

	public VisitException(String message) {
		super(message);
	}

	public VisitException(String message, Throwable cause) {
		super(message, cause);	
	}
	
	

}

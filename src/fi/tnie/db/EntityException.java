/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class EntityException
	extends Exception {

	protected EntityException() {
		super();
	}

	protected EntityException(String message, Throwable cause) {
		super(message, cause);	
	}	
}

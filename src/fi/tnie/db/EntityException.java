/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class EntityException
	extends Exception {

	public EntityException() {
		super();
	}
	public EntityException(String msg) {
		super(msg);
	}
	public EntityException(String message, Throwable cause) {
		super(message, cause);	
	}	
}

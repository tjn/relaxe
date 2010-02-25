/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class EntityQueryException
	extends EntityException {

	public EntityQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityQueryException(String msg) {
		super(msg);
	}
	

}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

public class EntityException
	extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3377579961567817185L;
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

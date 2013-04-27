/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

public class EntityQueryException
	extends EntityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -735561072396101481L;

	public EntityQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityQueryException(String msg) {
		super(msg);
	}
	

}

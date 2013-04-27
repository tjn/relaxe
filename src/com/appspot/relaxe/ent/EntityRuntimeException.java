/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

public class EntityRuntimeException
	extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6858541793942629046L;
	
	public EntityRuntimeException() {
		super();
	}
	public EntityRuntimeException(String msg) {
		super(msg);
	}
	public EntityRuntimeException(String message, Throwable cause) {
		super(message, cause);	
	}	
}

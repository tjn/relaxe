/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

public class EntityException
	extends Exception
	implements Serializable {
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

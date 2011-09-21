/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

@SuppressWarnings("serial")
public class IllegalIdentifierException 
	extends RuntimeException {
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected IllegalIdentifierException() {
	}
	
	public IllegalIdentifierException(String msg) {
		super(msg);		
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

@SuppressWarnings("serial")
public class IllegalIdentifierException 
	extends RuntimeException {
	public IllegalIdentifierException(String msg) {
		super(msg);		
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public abstract class SQLDataChangeStatement
	extends Statement {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1483155455397731504L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SQLDataChangeStatement() {
	}
				
	public SQLDataChangeStatement(Name name) {
		super(name);		
	}	
}

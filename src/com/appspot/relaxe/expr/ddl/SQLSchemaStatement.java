/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.Statement;

public abstract class SQLSchemaStatement
	extends Statement {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1483155455397731504L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SQLSchemaStatement() {
	}
				
	public SQLSchemaStatement(Name name) {
		super(name);		
	}	
}

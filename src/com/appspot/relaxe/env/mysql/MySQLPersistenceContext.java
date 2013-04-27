/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.mysql;

import com.appspot.relaxe.env.DefaultPersistenceContext;
import com.appspot.relaxe.env.GeneratedKeyHandler;

public class MySQLPersistenceContext
	extends DefaultPersistenceContext<MySQLImplementation> {
	
	private MySQLGeneratedKeyHandler generatedKeyHandler;
		
	public MySQLPersistenceContext() {
		this(new MySQLImplementation());
	}
		
	public MySQLPersistenceContext(MySQLImplementation implementation) {
		super(implementation);
	}
	
	@Override
	public GeneratedKeyHandler generatedKeyHandler() {
		if (generatedKeyHandler == null) {			
			generatedKeyHandler = new MySQLGeneratedKeyHandler(getValueExtractorFactory());
		}

		return generatedKeyHandler;
	}	
}

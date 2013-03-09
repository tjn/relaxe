/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.mysql;

import fi.tnie.db.env.DefaultPersistenceContext;
import fi.tnie.db.env.GeneratedKeyHandler;

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

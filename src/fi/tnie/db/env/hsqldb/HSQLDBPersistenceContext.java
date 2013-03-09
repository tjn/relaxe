/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.hsqldb;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.env.DefaultGeneratedKeyHandler;
import fi.tnie.db.env.DefaultPersistenceContext;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLGeneratedKeyHandler;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.env.pg.PGImplementation;

public class HSQLDBPersistenceContext
	extends DefaultPersistenceContext<HSQLDBImplementation> {

	private GeneratedKeyHandler generatedKeyHandler;
	
	public HSQLDBPersistenceContext() {
		this(new HSQLDBImplementation());
	}
		
	public HSQLDBPersistenceContext(HSQLDBImplementation implementation) {
		super(implementation);
	}
	
	@Override
	public GeneratedKeyHandler generatedKeyHandler() {
		if (generatedKeyHandler == null) {			
			generatedKeyHandler = new DefaultGeneratedKeyHandler(getValueExtractorFactory());
		}

		return generatedKeyHandler;
	}	

}

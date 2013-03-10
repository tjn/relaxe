/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.hsqldb;

import fi.tnie.db.env.DefaultGeneratedKeyHandler;
import fi.tnie.db.env.DefaultPersistenceContext;
import fi.tnie.db.env.GeneratedKeyHandler;

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

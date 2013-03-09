/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.env.DefaultPersistenceContext;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation.PGGeneratedKeyHandler;

public class PGPersistenceContext
	extends DefaultPersistenceContext<PGImplementation> {
	
	private PGGeneratedKeyHandler generatedKeyHandler;
		
	public PGPersistenceContext() {
		this(new PGImplementation());
	}
		
	public PGPersistenceContext(PGImplementation implementation) {
		super(implementation);
	}

	@Override
	public GeneratedKeyHandler generatedKeyHandler() {
		if (generatedKeyHandler == null) {			
			generatedKeyHandler = new PGGeneratedKeyHandler(getValueExtractorFactory());
		}

		return generatedKeyHandler;
	}	
}

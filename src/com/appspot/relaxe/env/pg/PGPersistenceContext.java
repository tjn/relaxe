/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.env.DefaultPersistenceContext;
import com.appspot.relaxe.env.GeneratedKeyHandler;
import com.appspot.relaxe.env.pg.PGImplementation.PGGeneratedKeyHandler;

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

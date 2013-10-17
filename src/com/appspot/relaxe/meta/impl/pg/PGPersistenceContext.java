/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.env.DefaultPersistenceContext;
import com.appspot.relaxe.env.GeneratedKeyHandler;

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

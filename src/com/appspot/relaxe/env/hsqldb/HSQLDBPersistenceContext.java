/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.hsqldb;

import com.appspot.relaxe.env.DefaultGeneratedKeyHandler;
import com.appspot.relaxe.env.DefaultPersistenceContext;
import com.appspot.relaxe.env.GeneratedKeyHandler;

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

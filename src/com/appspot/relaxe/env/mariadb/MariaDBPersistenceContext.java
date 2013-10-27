/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.mariadb;

import com.appspot.relaxe.env.DefaultPersistenceContext;
import com.appspot.relaxe.env.GeneratedKeyHandler;

public class MariaDBPersistenceContext
	extends DefaultPersistenceContext<MariaDBImplementation> {
	
	private MariaDBGeneratedKeyHandler generatedKeyHandler;
		
	public MariaDBPersistenceContext() {
		this(new MariaDBImplementation());
	}
		
	public MariaDBPersistenceContext(MariaDBImplementation implementation) {
		super(implementation);
	}
	
	@Override
	public GeneratedKeyHandler generatedKeyHandler() {
		if (generatedKeyHandler == null) {			
			generatedKeyHandler = new MariaDBGeneratedKeyHandler(getValueExtractorFactory());
		}

		return generatedKeyHandler;
	}	
}

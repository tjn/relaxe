/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.DBMetaTestCase;


public class PGTestCase
	extends DBMetaTestCase<PGImplementation> {

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PGPersistenceContext();
	}
	
	@Override
	protected String getDatabase() {
		return "pagila";
	}
	
//	@Override
//	protected String getUsername() {
//		return "relaxe";
//	}	
}

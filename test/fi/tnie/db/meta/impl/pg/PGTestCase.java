/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.env.pg.PGPersistenceContext;
import fi.tnie.db.meta.DBMetaTestCase;

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

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.env.mysql.MySQLPersistenceContext;
import fi.tnie.db.meta.DBMetaTestCase;

public class MySQLTestCase
	extends DBMetaTestCase<MySQLImplementation> {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLPersistenceContext();
	}
}

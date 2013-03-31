/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.mysql.test;


import java.io.IOException;
import java.util.Properties;

import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.env.mysql.MySQLPersistenceContext;

public abstract class AbstractMySQLTestCase
	extends AbstractUnitTest<MySQLImplementation> {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLPersistenceContext();
	}
			
	@Override
	protected Properties getJdbcConfig() throws IOException {		
		return getJdbcConfigForDatabase();
	}

	@Override
	protected String implementationTag() {
		return "mysql";
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.env.mysql.MySQLPersistenceContext;

public class MySQLTestCase
	extends AbstractUnitTest<MySQLImplementation> {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLPersistenceContext();
	}
	
	
	@Override
	public String getDatabase() {
		return "sakila";
	}
	
	@Override
	public String getUser() {
		return "sakila";
	}
}

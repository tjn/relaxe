/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql.sakila;

import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.mysql.test.AbstractMySQLTestCase;
import fi.tnie.db.test.SakilaPersistenceContext;

public abstract class SakilaTestCase
	extends AbstractMySQLTestCase {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new SakilaPersistenceContext();
	}
		
	@Override
	public String getDatabase() {
		return "sakila";
	}	
}

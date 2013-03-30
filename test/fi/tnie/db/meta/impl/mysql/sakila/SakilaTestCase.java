/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql.sakila;

import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.meta.impl.mysql.MySQLTestCase;
import fi.tnie.db.test.SakilaPersistenceContext;

public abstract class SakilaTestCase
	extends MySQLTestCase {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new SakilaPersistenceContext();
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

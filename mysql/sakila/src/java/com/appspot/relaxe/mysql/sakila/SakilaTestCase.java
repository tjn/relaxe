/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.sakila;

import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.mysql.AbstractMySQLTestCase;


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

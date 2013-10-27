/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mariadb.sakila;


import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mariadb.MariaDBImplementation;
import com.appspot.relaxe.mariadb.AbstractMariaDBTestCase;


public abstract class SakilaTestCase
	extends AbstractMariaDBTestCase {
	
	@Override
	protected PersistenceContext<MariaDBImplementation> createPersistenceContext() {
		return new SakilaPersistenceContext();
	}
		
	@Override
	public String getDatabase() {
		return "sakila";
	}	
}

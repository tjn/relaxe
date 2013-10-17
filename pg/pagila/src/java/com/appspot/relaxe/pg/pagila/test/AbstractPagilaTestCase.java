/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila.test;

import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.pg.AbstractPGTestCase;
import com.appspot.relaxe.pg.pagila.PagilaPersistenceContext;


public abstract class AbstractPagilaTestCase
	extends AbstractPGTestCase {

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PagilaPersistenceContext();
	}

	@Override
	public String getDatabase() {
		return "pagila";
	}
	

}

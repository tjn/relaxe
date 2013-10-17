/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.StatementExecutorTest;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.gen.pagila.ent.pub.Actor;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;


public class PagilaStatementExecutorTest
	extends StatementExecutorTest<PGImplementation> {

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PagilaPersistenceContext();
	}
		
	public void testFetch() throws Exception {
		BaseTable t = Actor.Type.TYPE.getMetaData().getBaseTable();
		super.testFetch(t);
	}

	@Override
	protected String getDatabase() {
		return "pagila";
	}	
	
	
}

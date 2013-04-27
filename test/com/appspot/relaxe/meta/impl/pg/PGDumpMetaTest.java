/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import java.io.IOException;
import java.sql.SQLException;

import com.appspot.relaxe.query.QueryException;



public class PGDumpMetaTest
	extends PGJDBCTestCase {
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}	

	public void testDumpMetaData() throws QueryException, SQLException {
		dumpMetaData();
	}
	
}

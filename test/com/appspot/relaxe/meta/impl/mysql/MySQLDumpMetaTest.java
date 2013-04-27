/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;

import com.appspot.relaxe.query.QueryException;


public class MySQLDumpMetaTest
	extends MySQLJDBCTestCase {
	
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}

	public void testDumpMetaData() throws QueryException, SQLException {
		dumpMetaData();
	}
	
	
}

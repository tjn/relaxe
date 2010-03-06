/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fi.tnie.db.QueryException;
import fi.tnie.db.meta.util.ResultSetWriter;
import fi.tnie.db.meta.util.StringListReader;

public class MySQLDumpMetaTest
	extends MySQLTestCase {
	
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}

	public void testDumpMetaData() throws QueryException, SQLException {
		dumpMetaData();
	}	
}

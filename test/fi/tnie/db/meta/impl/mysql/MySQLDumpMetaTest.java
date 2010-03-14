/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import java.io.IOException;
import java.sql.SQLException;
import fi.tnie.db.QueryException;

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

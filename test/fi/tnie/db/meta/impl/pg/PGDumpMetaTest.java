/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.IOException;
import java.sql.SQLException;

import fi.tnie.db.query.QueryException;


public class PGDumpMetaTest
	extends PGTestCase {
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}	

	public void testDumpMetaData() throws QueryException, SQLException {
		dumpMetaData();
	}
	
}

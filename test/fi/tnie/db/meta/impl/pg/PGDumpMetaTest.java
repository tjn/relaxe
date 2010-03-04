/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fi.tnie.db.QueryException;
import fi.tnie.db.meta.util.ResultSetWriter;
import fi.tnie.db.meta.util.StringListReader;

public class PGDumpMetaTest
	extends PGTestCase {
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}

	public void testDumpMeta() throws QueryException, SQLException {		
		Connection c = getConnection();		
		DatabaseMetaData meta = c.getMetaData();
		
		ResultSetWriter rw = new ResultSetWriter(System.out, false);
				
		rw.header("Catalogs");
		rw.apply(meta.getCatalogs());
				
		rw.header("Schemas");
		rw.apply(meta.getSchemas());		
		

		String[] types = { "TABLE" };
		
		{			
			ResultSet tables = meta.getTables(null, null, "%", types);
			rw.header("Base tables");
			rw.apply(tables);
		}
		
		
		ArrayList<String> tables = new ArrayList<String>();
		
		{
			StringListReader r = new StringListReader(tables, 3);
			r.apply(meta.getTables(null, null, "%", types));
		}
		
		for (String n : tables) {
				ResultSet columns = meta.getColumns(null, null, n, null);
				rw.header("Columns in table " + n);
				rw.apply(columns);
				
				ResultSet pkcols = meta.getPrimaryKeys(null, null, n);
				rw.header("Primary key columns in table " + n);
				rw.apply(pkcols);
				
				ResultSet fkcols = meta.getImportedKeys(null, null, n);
				rw.header("Foreign key column in table " + n);
				rw.apply(fkcols);												
		}
	}
	
	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.IOException;
import java.sql.SQLException;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.query.QueryException;

public class PGWellKnownTableTest
	extends PGJDBCTestCase {
	
	public static final String COL_CONTINENT_ID = "id";
	public static final String COL_COUNTRY_CONTINENT = "continent";
	public static final String COL_COUNTRY_ID = "id";
	public static final String FK_COUNTRY_CONTINENT = "fk_country_continent";
	
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}

	public void testWellKnownTables() throws QueryException, SQLException {
			BaseTable a = getCountryTable();
			BaseTable b = getContinentTable();
			
	//		testBaseTable(a);
	//		testBaseTable(b);
			
			assertNotSame(a, b);
			
			testPrimaryKey(a.getPrimaryKey());
			testPrimaryKey(b.getPrimaryKey());
			
			assertSame(a, a.getPrimaryKey().getTable());		
			assertSame(b, b.getPrimaryKey().getTable());
			
			assertEquals(3, a.columnMap().values().size());
			assertEquals(3, a.columns().size());
			
			assertEquals(2, b.columnMap().values().size());
			assertEquals(2, b.columns().size());		
			
			assertNotNull(a.foreignKeys());
			assertFalse(a.foreignKeys().values().isEmpty());
			
			String fkcc = FK_COUNTRY_CONTINENT;
			
			ForeignKey fk = a.foreignKeys().get(fkcc);		
			assertNotNull(fk);
									
//			ForeignKey sk = a.getSchema().foreignKeys().get(fkcc);
//			assertNotNull(sk);
							
//			assertSame(fk, sk);		
//			assertSame(sk, a.getSchema().constraints().get(fkcc));				
									
			Column cc = a.columnMap().get(COL_COUNTRY_CONTINENT);
			assertNotNull(cc);
			
			assertEquals(1, fk.columns().values().size());
			
			logger().debug("fk-cols: " + fk.columns());
							
			Column contID = fk.columns().get(cc);
			assertNotNull(contID);
					
			assertSame(a, fk.getReferencing()); 
			assertSame(b, fk.getReferenced());
					
	//		PrimaryKey pk = getCountryTable().getPrimaryKey();
	//		assertNotNull(pk);
	//		testBaseTable(getCountryTable());
			
					
		}
	
	
}

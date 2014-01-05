/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.meta.impl.pg;

import java.io.IOException;
import java.sql.SQLException;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.query.QueryException;


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
			
			assertEquals(3, a.getColumnMap().values().size());
//			assertEquals(3, a.().size());
			
			assertEquals(2, b.getColumnMap().values().size());
//			assertEquals(2, b.columns().size());		
			
			assertNotNull(a.foreignKeys());
			assertFalse(a.foreignKeys().values().isEmpty());
			
			String fkcc = FK_COUNTRY_CONTINENT;
			
			ForeignKey fk = a.foreignKeys().get(fkcc);		
			assertNotNull(fk);
									
//			ForeignKey sk = a.getSchema().foreignKeys().get(fkcc);
//			assertNotNull(sk);
							
//			assertSame(fk, sk);		
//			assertSame(sk, a.getSchema().constraints().get(fkcc));				
									
			Column cc = a.getColumnMap().get(COL_COUNTRY_CONTINENT);
			assertNotNull(cc);
			
			assertEquals(1, fk.getColumnMap().values().size());
			
			logger().debug("fk-cols: " + fk.getColumnMap().values());
							
			Column contID = fk.getReferenced(cc);
			assertNotNull(contID);
					
			assertSame(a, fk.getReferencing()); 
			assertSame(b, fk.getReferenced());
					
	//		PrimaryKey pk = getCountryTable().getPrimaryKey();
	//		assertNotNull(pk);
	//		testBaseTable(getCountryTable());
			
					
		}
	
	
}

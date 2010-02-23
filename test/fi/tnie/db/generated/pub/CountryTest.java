/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.generated.pub;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.DefaultEntityContext;
import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.EntityContext;
import fi.tnie.db.QueryException;
import fi.tnie.db.TableMapper;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.impl.pg.PGTestCase;

public class CountryTest extends PGTestCase {

	@Override
	protected void setUp() throws Exception {
		restore();
		super.setUp();
	}	
	
	public void testInsert() 
		throws SQLException, QueryException {		
		Connection c = getConnection();		
		c.setAutoCommit(false);
		
		Catalog catalog = getCatalog();
		
		TableMapper tm = new DefaultTableMapper("fi.tnie.db.generated");		
		new DefaultEntityContext(catalog, tm);
		
		Continent a = new ContinentImpl();		
		a.set(Continent.Attribute.NAME, "Asia");
		a.insert(c);
		
		Continent e = new ContinentImpl();
		e.set(Continent.Attribute.NAME, "Europe");
		e.insert(c);				
		
		{
			Country fi = new CountryImpl();
			fi.set(Country.Attribute.NAME, "Finland");
			fi.set(Country.Reference.CONTINENT, e);
			fi.insert(c);
		}
		
		c.commit();
		
		try {
			Country fi = new CountryImpl();
			fi.set(Country.Attribute.NAME, "Finland");
			fi.set(Country.Reference.CONTINENT, null);
			fi.insert(c);
			assertThrown(SQLException.class);
		}
		catch (SQLException se) {
			// "23502" An insert or update value is null, but the column cannot contain null values.
			logger().info(se.getMessage());						
			assertEquals("23502", se.getSQLState());			
		}
	}

}

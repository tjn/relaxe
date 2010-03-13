/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.generated.pub;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.EntityException;
import fi.tnie.db.QueryException;
import fi.tnie.db.meta.impl.pg.PGTestCase;
import fi.tnie.db.myapp.pub.Continent;
import fi.tnie.db.myapp.pub.Country;
import fi.tnie.db.myapp.pub.PublicFactory;
import fi.tnie.db.myapp.CatalogContext;

public class AppTest extends PGTestCase {

//	private static final String GENERATED_PACKAGE = "fi.tnie.db.myapp";
		
//	protected EntityContext bindAll(Catalog catalog) {
//	    
//		DefaultEntityContext ec = new DefaultEntityContext(catalog);		
//		DefaultTableMapper tm = new DefaultTableMapper(GENERATED_PACKAGE);		
//		ec.bindAll(tm);
//		
//		return ec;
//	}

	@Override
	protected void setUp() throws Exception {
		restore();
		super.setUp();
//		bindAll(getCatalog());
	}	
	
	public void testInsertAndUpdate() 
		throws SQLException, QueryException, EntityException {
		
		Connection c = getConnection();		
		c.setAutoCommit(false);
					
		CatalogContext cc = new CatalogContext(getCatalog());
		PublicFactory ef = cc.newPublicFactory();		
									
		Continent a = ef.newContinent();
		a.set(Continent.Attribute.NAME, "Asia");
		a.insert(c);
		
		Continent e = ef.newContinent();
		e.set(Continent.Attribute.NAME, "Europe");
		e.insert(c);
		
		final Country fi = ef.newCountry();
		
		{			
			fi.set(Country.Attribute.NAME, "Finland");
			fi.set(Country.Reference.CONTINENT, e);
			fi.insert(c);
		}
		
		c.commit();
		
		try {
			Country fi2 = ef.newCountry();
			fi2.set(Country.Attribute.NAME, "Finland");
			fi2.set(Country.Reference.CONTINENT, null);
			fi2.insert(c);
			assertThrown(SQLException.class);
		}
		catch (EntityException ee) {
			SQLException se = (SQLException) ee.getCause();			
			// "23502" An insert or update value is null, but the column cannot contain null values.
			logger().info(se.getMessage());						
			assertEquals("23502", se.getSQLState());			
		}
		
		c.rollback();
		
		fi.set(Country.Attribute.NAME, "Suomi");
		fi.set(Country.Reference.CONTINENT, a);
		fi.update(c);		
		c.commit();
	}
}

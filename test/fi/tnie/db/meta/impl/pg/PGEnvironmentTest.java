/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;

import fi.tnie.db.expr.DelimitedIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.CatalogFactory;

public class PGEnvironmentTest 
	extends PGTestCase {
		
	public void testCatalogFactory() 
		throws Exception {
		CatalogFactory cf = newEnv().catalogFactory();		
		assertNotNull("CatalogFactory was null", cf);
	}
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
	}
	
	private PGEnvironment newEnv() 
		throws SQLException {		
		Connection c = getConnection();		
		return new PGEnvironment(c.getMetaData());
	}

	public void testCreateIdentifier() 
		throws Exception {

		PGEnvironment env = newEnv();		
		 
		final Identifier a = env.createIdentifier("abc");		
		assertTrue(a.isOrdinary());
				
		final Identifier b = env.createIdentifier("Abc");
		assertTrue(b.isOrdinary());

		final Identifier c = env.createIdentifier("ABC");
		assertTrue(c.isOrdinary());

		final Identifier d = env.createIdentifier("Mary's");
		assertFalse("Delimited expected", d.isOrdinary());	
	}
	
	
	public void testIllegalIdentifier() throws Exception {
		PGEnvironment env = newEnv();
		
		try {
			env.createIdentifier("");
			assertThrown(IllegalIdentifierException.class);
		}
		catch (IllegalIdentifierException e) {
//			OK, expected
		}
		
		try {
			env.createIdentifier(null);
			assertThrown(NullPointerException.class);
		}
		catch (NullPointerException e) {
//			OK, expected
		}		
		
		Identifier id = env.createIdentifier("1A");
		assertFalse(id + " is not not valid as an ordinary", id.isOrdinary());
	}
	

	public void testIdentifierComparator() 
		throws SQLException {
		
		PGEnvironment env = newEnv();
		Comparator<Identifier> icmp = newEnv().createIdentifierComparator();
			
		{
			final Identifier a = env.createIdentifier("abc");
			assertTrue(a.isOrdinary());
			
			// test PostgreSQL-specific identifier folding:
			final Identifier b = new DelimitedIdentifier("abc");
			assertTrue(icmp.compare(a, b) == 0);
			
			final Identifier c = new DelimitedIdentifier("ABC");
			assertTrue(icmp.compare(a, c) != 0);
			
			assertTrue(icmp.compare(b, c) != 0);
			
			assertTrue(icmp.compare(b, new DelimitedIdentifier(b.getName())) == 0);
			assertTrue(icmp.compare(c, new DelimitedIdentifier(c.getName())) == 0);
		}			
	}

}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;

import fi.tnie.db.expr.DelimitedIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Constraint;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaElementMap;
import fi.tnie.db.meta.SchemaMap;
import fi.tnie.db.meta.Table;

public class PGEnvironmentTest 
	extends PGTestCase {
	
	public static final String SCHEMA_PUBLIC = "public";
	public static final String TABLE_COUNTRY = "country";
	public static final String TABLE_CONTINENT = "continent";

	private Catalog catalog = null;
	
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
	
	
	public void testCatalogFactory() throws Exception {
		PGEnvironment env = newEnv();
		CatalogFactory cf = env.catalogFactory();
		
		assertNotNull(cf);		
		Connection c = getConnection();
		
		Catalog catalog = cf.create(c.getMetaData(), getDatabase());
		assertNotNull(catalog);
		
		SchemaMap sm = catalog.schemas();
		assertNotNull(sm);
				
		Schema sp = sm.get("public");
		
		SchemaElementMap<? extends Table> tables = sp.tables();		
		assertNotNull(tables);
		assertNotNull(tables.values());
		
		SchemaElementMap<? extends BaseTable> baseTables = sp.baseTables();
		assertNotNull(baseTables);
		assertNotNull(baseTables.values());
		
		HashSet<Table> ts = new HashSet<Table>(tables.values());
		HashSet<Table> bs = new HashSet<Table>(baseTables.values());
				
		assertTrue(ts.containsAll(bs));
		
		for (Table table : ts) {
			if (table.isBaseTable()) {
				assertTrue("Expected baseTables to contain: " + table, bs.contains(table));
			}
		}
		
		
		{
			Table country = tables.get(TABLE_COUNTRY);
			assertNotNull(country);
			
			Table continent = tables.get(TABLE_CONTINENT);
			assertNotNull(continent);
		}
		
		assertTrue(!baseTables.keySet().isEmpty());
		
		BaseTable country = baseTables.get(TABLE_COUNTRY);
		BaseTable continent = baseTables.get(TABLE_CONTINENT);
		
		assertNotNull(country);
		ForeignKey fk = country.foreignKeys().get("fk_ctry_continent");
		assertNotNull(fk);		
		assertNotNull(fk.getReferenced());
		assertNotNull(fk.getReferencing());
		
		assertSame(continent, fk.getReferenced());
		assertSame(country, fk.getReferencing());		
	}
	
	private Catalog getCatalog() 
		throws SQLException {

		if (catalog == null) {
			PGEnvironment env = newEnv();
			CatalogFactory cf = env.catalogFactory();
			
			assertNotNull(cf);		
			Connection c = getConnection();
			
			catalog = cf.create(c.getMetaData(), getDatabase());
			assertNotNull(catalog);
			
		}
		return catalog;
	}

	public void testForeignKeys() throws Exception {		
		SchemaMap sm = getCatalog().schemas();
		assertNotNull(sm);		
		
		for (Schema s : sm.values()) {
			if (s.getUnqualifiedName().equals(id(SCHEMA_PUBLIC))) {
				assertFalse(s.foreignKeys().values().isEmpty());
			}
			
			for (ForeignKey fk : s.foreignKeys().values()) {
				testForeignKey(fk);				
			}
		}
	}

	public void testPrimaryKeys() throws Exception {		
		SchemaMap sm = getCatalog().schemas();
		assertNotNull(sm);
		
		assertFalse(sm.values().isEmpty());
		
		for (Schema s : sm.values()) {
			if (s.getUnqualifiedName().equals(id(SCHEMA_PUBLIC))) {
				assertFalse(s.primaryKeys().values().isEmpty());
			}
			
			for (PrimaryKey pk : s.primaryKeys().values()) {
				testPrimaryKey(pk);
			}
		}
	}
	
	private Identifier id(String name) 
		throws IllegalIdentifierException, NullPointerException, SQLException {
		return getCatalog().getEnvironment().createIdentifier(name);
	}
	
	public void testConstraints() throws Exception {
				
		SchemaMap sm = getCatalog().schemas();
		assertNotNull(sm);
		
		assertFalse(sm.values().isEmpty());
		
		for (Schema s : sm.values()) {
			SchemaElementMap<? extends Constraint> cm = s.constraints();
						
			for (PrimaryKey pk : s.primaryKeys().values()) {
				assertSame(pk, cm.get(pk.getUnqualifiedName()));
			}
			
			for (ForeignKey fk : s.foreignKeys().values()) {
				assertSame(fk, cm.get(fk.getUnqualifiedName()));
			}
		
			HashSet<Constraint> cs = new HashSet<Constraint>(cm.values());
			HashSet<Constraint> pks = new HashSet<Constraint>(s.primaryKeys().values());
			HashSet<Constraint> fks = new HashSet<Constraint>(s.foreignKeys().values());
			
			HashSet<Constraint> all = new HashSet<Constraint>();
			all.addAll(pks);
			all.addAll(fks);
		
			cs.equals(all);
		}
		
		
	}
}

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
import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Constraint;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaElementMap;
import fi.tnie.db.meta.SchemaMap;
import fi.tnie.db.meta.Table;

public class PGEnvironmentTest 
	extends PGTestCase {
	
	@Override
	public void restore() throws IOException, InterruptedException {
		// no need to restore
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
				
		assertNull(env.createIdentifier(null));
		
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
		
		CatalogMap cm = cf.create(c);
		assertNotNull(cm);
		
		Catalog catalog = cm.get(c.getCatalog());						
		assertNotNull(catalog);
		
		SchemaMap sm = catalog.schemas();
		assertNotNull(sm);
				
		Schema sp = sm.get(SCHEMA_PUBLIC);
		
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
		
		assertTrue(!baseTables.keySet().isEmpty());
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
	
	public void testColumns() throws Exception {
		BaseTable t = getCountryTable();
		
		assertNotNull(t.columnMap());
		assertNotNull(t.columns());
		
		assertTrue(t.columnMap().keySet().size() > 1);
		assertTrue(t.columns().size() > 1);
		
		PrimaryKey pk = t.getPrimaryKey();
//		pk.getColumn(name)
					
		for (Column c : t.columns()) {
//			System.err.println(c.getUnqualifiedName() + ": " + c.getClass());
			assertNotNull(c);			
			assertNotNull(c.getColumnName());
			assertNotNull(c.getDataType());
			assertNotNull(c.getDataType().getTypeName());			
			assertNotNull(c.getUnqualifiedName());
						
		}
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

	protected void testBaseTable(BaseTable t) {
				
		assertNotNull(t);
		assertNotNull(t.getName());		
		assertNotNull(t.getName().getUnqualifiedName());				
		assertNotNull(t.getQualifiedName());
				
		assertFalse(t.getQualifiedName().trim().equals(""));
		logger().warn("table: " + t.getQualifiedName());
		
		assertNotNull(t.getUnqualifiedName());		
		assertNotNull(t.getUnqualifiedName().getName());
		assertTrue(t.getQualifiedName().contains(t.getUnqualifiedName().getName()));
				
		assertNotNull(t.getSchema());
				
		PrimaryKey pk = t.getPrimaryKey();	
		
		if (pk == null) {
			logger().warn("no primary key in table: " + t.getQualifiedName());
		}
		else {
			testPrimaryKey(pk);
			assertNotNull(pk.getTable());
			assertSame(pk.getTable(), t);			
		}
				
		SchemaElementMap<ForeignKey> fks = t.foreignKeys();
		assertNotNull(fks);
		assertNotNull(fks.values());
		
		for (ForeignKey fk : fks.values()) {
			testForeignKey(fk);
		}
	}

	public void testBaseTables() throws Exception {		
		SchemaMap sm = getCatalog().schemas();
		assertNotNull(sm);
		
		for (Schema s : sm.values()) {			
			for (BaseTable t : s.baseTables().values()) {				
				testBaseTable(t);				
			}
		}
	}
}

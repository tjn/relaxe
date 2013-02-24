/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.expr.DelimitedIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.query.QueryException;


public abstract class CatalogTest<I extends Implementation<I>> 
    extends DBMetaTestCase<I> {

    
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
    			
//    	assertNotNull(t.getSchema());
    	
    	assertNotNull(t.getName());
    	assertNotNull(t.getName().getQualifier());
    	assertNotNull(t.getName().getQualifier().getSchemaName());
    	assertNotNull(t.getName().getQualifier().getSchemaName().getName());
    	assertNotNull(t.getQualifiedName());    	
    			
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

    public void testColumns() throws Exception {
    		BaseTable t = getCountryTable(getCatalog());
    		
    		assertNotNull(t.columnMap());
    		assertNotNull(t.columns());
    		
    		assertTrue(t.columnMap().keySet().size() > 1);
    		assertTrue(t.columns().size() > 1);
    		
//    		PrimaryKey pk = t.getPrimaryKey();
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

    public void testIdentifierComparator() 
    	throws SQLException {
    	
    	Implementation<?> env = getEnvironmentContext().getImplementation();
    	Comparator<Identifier> icmp = env.identifierComparator();
    		
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

    protected Identifier id(String name) 
    	throws IllegalIdentifierException, NullPointerException, QueryException, SQLException {
    	return getCatalog().getEnvironment().createIdentifier(name);
    }
}

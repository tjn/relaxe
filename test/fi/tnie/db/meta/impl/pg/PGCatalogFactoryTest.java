/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGCatalogFactory;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.meta.SerializableEnvironment;
import fi.tnie.db.meta.impl.DefaultCatalogMap;
import fi.tnie.db.meta.impl.DefaultMutableCatalog;
import fi.tnie.db.meta.impl.DefaultMutableSchema;

public class PGCatalogFactoryTest extends DBMetaTestCase {

    public void testGetCatalogNameFromSchemas() 
        throws SQLException {
        assertNotNull(getEnvironmentContext());
                
        DatabaseMetaData meta = meta();                
        PGCatalogFactory f = factory();
        
        // f.prepare(meta);
                
        String catalog = getConnection().getCatalog();
        assertNotNull(catalog);
        
        String n = null;
        
        ResultSet schemas = meta.getSchemas();
        assertTrue(schemas.next());
        n = f.getCatalogNameFromSchemas(meta, schemas);
        assertNotNull(n);
        assertEquals(catalog, n);        
        schemas.close();
        
        String[] types = { "TABLE" };
        ResultSet tables = meta.getTables(null, null, "%", types);
        assertTrue(tables.next());
        n = f.getCatalogNameFromTables(meta, tables);        
        assertEquals(catalog, n);
        tables.close();
                
    }

    public void testGetSchemaNameFromSchemas() throws SQLException {
        DatabaseMetaData meta = meta();
        PGCatalogFactory f = factory();
        // f.prepare(meta);
                
        ResultSet schemas = f.getSchemas(meta);
        Set<String> names = new HashSet<String>();
        
        while (schemas.next()) {
            names.add(f.getCatalogNameFromSchemas(meta, schemas));
        }
                        
        assertContainsCatalog(names);        
    }

    public void testGetSchemaNameFromPKs() throws SQLException {
        DatabaseMetaData meta = meta();
        PGCatalogFactory f = factory();
        // f.prepare(meta);
                
        ResultSet pkcols = meta.getPrimaryKeys(null, null, TABLE_CONTINENT);
        Set<String> names = new HashSet<String>();
        
        while (pkcols.next()) {
            String pkcat = f.getCatalogNameFromPrimaryKeys(meta, pkcols);
            assertNotNull(pkcat);
            names.add(pkcat);
            String pksch = f.getSchemaNameFromPrimaryKeys(meta, pkcols);
            assertNotNull(pksch);
                        
        }
        
        pkcols.close();
        
        assertContainsCatalog(names);        
    }

    private void assertContainsCatalog(Set<String> names) throws SQLException {
        Connection c = getConnection();
        String catalog = c.getCatalog();
        assertNotNull(catalog);
        assertFalse(catalog.trim().equals(""));        
        assertTrue(catalog + " in " + names, names.contains(catalog));        
    }


    
//    public void testGetSchemaNameFromTables() {
//        fail("Not yet implemented");
//    }
//
//    public void testGetCatalogNameFromTables() {
//        fail("Not yet implemented");
//    }
//
//    public void testGetCatalogPattern() {
//        fail("Not yet implemented");
//    }
//
//    public void testGetSchemaPattern() {
//        fail("Not yet implemented");
//    }
//
//    public void testGetSchemaDefaultMutableCatalogStringString() {
//        fail("Not yet implemented");
//    }
//
//    public void testMySQLCatalogFactoryMySQLEnvironment() {
//        fail("Not yet implemented");
//    }
//
//    public void testMySQLCatalogFactoryMySQLEnvironmentString() {
//        fail("Not yet implemented");
//    }

    private DatabaseMetaData meta() throws SQLException {        
        return getConnection().getMetaData();
    }
    
    @Override
	public PGCatalogFactory factory() {
        PGImplementation e = new PGImplementation();
        return new PGCatalogFactory(e.getEnvironment());
    }
    
    public void testCreate() 
        throws Exception {        
        PGCatalogFactory factory = factory();
        testCatalogFactory(factory, getConnection());
    }
    
    
    public void testCreateCatalog() 
        throws Exception {                
        Implementation impl = getEnvironmentContext().getImplementation();
        PGCatalogFactory factory = factory();                
        Connection c = getConnection();        
        String current = c.getCatalog();
        DatabaseMetaData meta = meta();
        
        SerializableEnvironment env = impl.environment();
        
        
                       
        final DefaultMutableCatalog cat = new DefaultMutableCatalog(env, current);
        final DefaultCatalogMap cm = new DefaultCatalogMap(env);
        cm.add(cat);
                    
        DefaultMutableSchema schema = factory.createSchema(cat, impl.createIdentifier(SCHEMA_PUBLIC), meta);
        assertNotNull(schema);
        
        factory.prepare(meta);
        factory.populateSchema(schema, meta);               
        
        // factory.populateTables(meta, cm);        
        factory.populatePrimaryKeys(cm, meta);
        factory.populateForeignKeys(cm, meta);        
    }
    
    
    public void testPrepare() 
        throws SQLException {
        PGCatalogFactory f = factory();
        Connection c = getConnection();
        assertNotNull(c);
        String catalog = c.getCatalog();
        assertNotNull(catalog);
        
        f.prepare(c.getMetaData());
        
        try {
            f.prepare(c.getMetaData());
            assertThrown(IllegalStateException.class);
        }
        catch (IllegalStateException e) {
            // OK
        }
        
        
    }
    
}

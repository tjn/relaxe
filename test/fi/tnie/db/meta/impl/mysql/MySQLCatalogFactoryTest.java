/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.env.CatalogFactory;
import fi.tnie.db.env.DefaultCatalogFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.mysql.MySQLCatalogFactory;
import fi.tnie.db.env.mysql.MySQLImplementation;
import fi.tnie.db.env.mysql.MySQLPersistenceContext;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.meta.SerializableEnvironment;
import fi.tnie.db.meta.impl.DefaultCatalogMap;
import fi.tnie.db.meta.impl.DefaultMutableCatalog;
import fi.tnie.db.meta.impl.DefaultMutableSchema;

public class MySQLCatalogFactoryTest extends DBMetaTestCase<MySQLImplementation> {

	
    public void testGetCatalogNameFromSchemas() 
        throws SQLException {
        assertNotNull(getEnvironmentContext());
                
        DatabaseMetaData meta = meta();                
        MySQLCatalogFactory f = factory();
        
        String n = null;
        
        ResultSet schemas = meta.getSchemas();
        n = f.getCatalogNameFromSchemas(meta, schemas);
        assertNull(n);        
        schemas.close();
        
        ResultSet tables = meta.getSchemas();
        n = f.getCatalogNameFromTables(meta, tables);        
        assertNull(n);
        tables.close();
                
    }

    public void testGetSchemaNameFromSchemas() throws SQLException {
        DatabaseMetaData meta = meta();
        DefaultCatalogFactory f = factory();
                
        ResultSet schemas = f.getSchemas(meta);
        Set<String> names = new HashSet<String>();
        
        while (schemas.next()) {
            names.add(f.getSchemaNameFromSchemas(meta, schemas));
        }
                        
        assertContainsCatalog(names);        
    }

    public void testGetSchemaNameFromPKs() throws SQLException {
        DatabaseMetaData meta = meta();
        DefaultCatalogFactory f = factory();
                
        ResultSet pkcols = meta.getPrimaryKeys(null, null, "continent");
        Set<String> names = new HashSet<String>();
        
        while (pkcols.next()) {
            String pkcat = f.getCatalogNameFromPrimaryKeys(meta, pkcols);
            assertNull(pkcat);
            String pksch = f.getSchemaNameFromPrimaryKeys(meta, pkcols);
            assertNotNull(pksch);
            names.add(pksch);            
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
	public MySQLCatalogFactory factory() {
        MySQLEnvironment e = new MySQLEnvironment();
        return new MySQLCatalogFactory(e);
    }
    
    public void testCreate() 
        throws Exception {        
        CatalogFactory factory = factory();        
        testCatalogFactory(factory, getConnection());
    }
    
    public void testCreateCatalog() 
        throws Exception {                
    	Implementation<?> impl = getEnvironmentContext().getImplementation();
        DefaultCatalogFactory factory = factory();                
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
    
//    @Override
//	public MySQLCatalogFactory factory() {        
//        return new MySQLCatalogFactory(implementation().environment());        
//    }    
    
    @Override
    protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
       	return new MySQLPersistenceContext();
    }

	@Override
	protected String getDatabase() {
		return "sakila";
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultCatalogFactory2;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mysql.MySQLCatalogFactory;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.env.mysql.MySQLPersistenceContext;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.DBMetaTestCase;
import com.appspot.relaxe.meta.impl.mysql.MySQLEnvironment;
import com.appspot.relaxe.query.QueryException;


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
        DefaultCatalogFactory2 f = factory();
                
        ResultSet schemas = f.getSchemas(meta);
        Set<String> names = new HashSet<String>();
        
        while (schemas.next()) {
            names.add(f.getSchemaNameFromSchemas(meta, schemas));
        }
                        
        assertContainsCatalog(names);        
    }

    public void testGetSchemaNameFromPKs() throws SQLException {
        DatabaseMetaData meta = meta();
        MySQLCatalogFactory f = factory();
                
        ResultSet pkcols = meta.getPrimaryKeys(null, null, TABLE_LANGUAGE);
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

    @Override
    protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
       	return new MySQLPersistenceContext();
    }

	@Override
	protected String getDatabase() {
		return "sakila";
	}
	
	@Override
	protected BaseTable getWellKnownBaseTable(Catalog cat, String schema, String table) 	
		throws QueryException, SQLException {
		return super.getWellKnownBaseTable(cat, getDatabase(), table);
	}
	
}

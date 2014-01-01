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

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.appspot.relaxe.StatementExecutor;
import com.appspot.relaxe.env.util.ResultSetWriter;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;


public class PGCatalogFactoryTest 
	extends PGTestCase {
	
//	private PGImplementation implementation = new PGImplementation();

    public void testGetCatalogNameFromSchemas() 
        throws SQLException, QueryException {
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
        
        {
	        String[] types = { "TABLE" };
	        ResultSet tables = meta.getTables(null, null, "%", types);
	        assertTrue(tables.next());
	        n = f.getCatalogNameFromTables(meta, tables);        
	        assertEquals(catalog, n);
	        tables.close();
        }
        
        
        {
        	String[] types = { "VIEW", "SYSTEM TABLE" };
	        ResultSet at = meta.getTables(null, null, "%", types);
	        
	        
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        ResultSetWriter w = new ResultSetWriter(os, true);
	                
	        
	        StatementExecutor se = new StatementExecutor(getPersistenceContext());
	        se.apply(w, at);
	        
	        String rs = new String(os.toByteArray());
	        logger().debug("testGetCatalogNameFromSchemas: tables\n" + rs);
	        
	                
	        assertEquals(catalog, n);
	        at.close();
        }
        
        Catalog cat = f.create(getConnection());
                
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
                
        ResultSet pkcols = meta.getPrimaryKeys(null, null, TABLE_FILM);
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
    }

/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.meta.impl.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultCatalogFactory;
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
        MySQLEnvironment e = MySQLEnvironment.environment();
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

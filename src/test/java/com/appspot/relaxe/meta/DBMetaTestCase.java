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
package com.appspot.relaxe.meta;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.DBMetaTest;
import com.appspot.relaxe.DefaultTableMapper;
import com.appspot.relaxe.DefaultTestContext;
import com.appspot.relaxe.EnvironmentTestContext;
import com.appspot.relaxe.HasTestContext;
import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.SimpleTestContext;
import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.meta.SchemaMap;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.types.ReferenceType;

import junit.framework.TestCase;

public abstract class DBMetaTestCase<I extends Implementation<I>> 
    extends TestCase
    implements DBMetaTest, HasTestContext<I> {
    
    public static final String SCHEMA_PUBLIC = "public";
    public static final String TABLE_LANGUAGE = "language";
    public static final String TABLE_FILM = "film";
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    private EnvironmentTestContext environmentContext = null;
    
    private Connection connection = null;    
    private DefaultTableMapper tableMapper;
    
    private ClassLoader classLoaderForGenerated = null;
    
    private TestContext<I> testContext;
    private PersistenceContext<I> persistenceContext;
    
    private List<Connection> connectionList = new ArrayList<Connection>();
          
    protected int read(ResultSet rs, int col, Collection<String> dest) 
        throws SQLException {
        int count = 0;
        
        while(rs.next()) {
            dest.add(rs.getString(col));
            count++;
        }
        
        return count;
    }
    
    

    public void testCatalogFactory(CatalogFactory cf, Connection c) throws Exception {    	
    	assertNotNull(cf);
    	long s = System.currentTimeMillis();
    	Catalog catalog = cf.create(c);
    	long elapsed = System.currentTimeMillis() - s;
    	logger().debug("testCatalogFactory: elapsed=" + elapsed);
    					
    	assertNotNull(catalog);
    	
    	testCatalog(catalog, c);    	
    }
    
    public void testCatalog(Catalog catalog, Connection c) throws Exception {     
                            
        assertNotNull(catalog);
        
        SchemaMap sm = catalog.schemas();
        assertNotNull(sm);
        
        assertFalse(sm.keySet().isEmpty());
    
        logger().debug("schemas: " + sm.keySet());      
                
        Schema sp = sm.get(SCHEMA_PUBLIC);
      
		  if (sp == null) {
		      // MySQL test database does not currently contain the schema public  
		      sp = sm.get(c.getCatalog());
		  }
        
        assertNotNull(sp);
        
        SchemaElementMap<? extends Table> tables = sp.tables();     
        assertNotNull(tables);
        assertNotNull(tables.values());
        
        BaseTable language = getWellKnownBaseTable(catalog, SCHEMA_PUBLIC, TABLE_LANGUAGE);
        assertNotNull(language);

        BaseTable film = getWellKnownBaseTable(catalog, SCHEMA_PUBLIC, TABLE_FILM);
        assertNotNull(film);

        SchemaElementMap<BaseTable> baseTables = sp.baseTables();
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
        
        
        assertFalse(sm.isEmpty());
        assertTrue(sm.size() > 0);
        
                        
        for (Schema schema : sm.values()) {
        	assertNotNull(schema);
        	        	
        	SchemaElementMap<PrimaryKey> pkmap = schema.primaryKeys();
        	assertNotNull(pkmap);
        	        	        	
        	for (PrimaryKey pk : pkmap.values()) {
				assertNotNull(pk);
				assertNotNull(pk.getTable());
			}
        	
        	SchemaElementMap<ForeignKey> fkmap = schema.foreignKeys();
        	assertNotNull(fkmap);
        	
        	for (ForeignKey fk : fkmap.values()) {
        		assertNotNull(fk);
        		assertNotNull(fk.getReferencing());
        		assertNotNull(fk.getReferenced());				
			}        	
        }
        
        
        
        assertTrue(!baseTables.keySet().isEmpty());
    }
    public Logger logger() {
//        return DBMetaTestCase.logger;
        return this.logger;
    }

    public void init(I impl) {
    	init(new SimpleTestContext<I>(impl, null, getDatabase(), getUsername(), getPassword()));
    }
       
    /**
	 * Get the password to copy to jdbc properties.  
	 * 
	 * Default implementation return the value <code>password</code>.
	 * 
	 * @return
	 */
	protected String getPassword() {
		return "password";
	}

	/**
	 * Get the username to copy to jdbc properties.  
	 * 
	 * Defaults to <code>getDatabase()</code>
	 * 
	 * @return
	 */
	protected String getUsername() {		
		return getDatabase();
	}


	protected abstract String getDatabase();
	

	@Override
    public void init(EnvironmentTestContext ctx) {
        
        logger().debug("\n\ninit()");
        Driver drv = ctx.getDriver();
        logger().debug(drv.getClass().toString());
        logger().debug(drv.getMajorVersion() + "." + drv.getMinorVersion());
        logger().debug("\n\n");
        this.environmentContext = ctx;
    }

    public EnvironmentTestContext getEnvironmentContext() {
        logger().debug("getContext: " + this.environmentContext + " for " + id());
        return environmentContext;
    }
    
    
    @Override
	public String id() {
        return getClass().getName() + "@" + System.identityHashCode(this);
    }
    
    private Connection connect() 
        throws SQLException {
        assertNotNull(this.environmentContext);
        Connection c = this.environmentContext.connect();
        assertNotNull(c);
        return c;
    }
    
    public Connection newConnection() throws SQLException {
    	Connection c = connect();
    	this.connectionList.add(c);
    	return c;
    }
    
    
    @Override
    protected void setUp() throws Exception {
    	init(implementation());
    	
        super.setUp();
        this.connection = connect();
        this.connection.setAutoCommit(false);
    }
    
    protected I implementation() {
    	return getPersistenceContext().getImplementation();
    }
    
    protected PersistenceContext<I> getPersistenceContext() {
    	if (persistenceContext == null) {
			persistenceContext = createPersistenceContext();			
		}

		return persistenceContext;
    }
    
    
    protected abstract PersistenceContext<I> createPersistenceContext();
    
    
	@Override
    protected void tearDown() throws Exception {
		for (Connection c : connectionList) {
			QueryHelper.doClose(c);
		}		
		
		connectionList.clear();		
		
        this.connection = QueryHelper.doClose(this.connection);
        super.tearDown();
    }

    public Connection getConnection() {
        return connection;
    }

    protected <E extends Exception> void assertThrown(Class<? extends E> e) {
        fail(e + " was not thrown");
    }

    protected void testForeignKey(ForeignKey fk) {
    		assertNotNull(fk);
    		assertNotNull(fk.getReferenced());
    		assertNotNull(fk.getReferencing());
    		assertFalse(fk.getColumnMap().isEmpty());
    		
    		ColumnMap cm = fk.getColumnMap();
    		
    		for (Column col : cm.values()) {
    			
    			assertNotNull(col);
    			Column rcol = fk.getReferenced(col);
    			assertNotNull(rcol);
    			
    			assertNotSame(col, rcol);
    			
    			logger().debug("ref'ing: " + col.getUnqualifiedName().getName());
    			logger().debug("ref'ed: " + rcol.getUnqualifiedName().getName());
    			
    			assertNotNull(fk.getReferencing().columnMap().get(col.getUnqualifiedName()));
    			assertNotNull(fk.getReferenced().columnMap().get(rcol.getUnqualifiedName()));
    								
    			// TODO: 
    			// it should be enough for all referenced columns to be
    			// part of the same candidate key (not necessarily a primary key),
    			// but we have no representation for candidate key at the moment 
    //			assertTrue(p.getValue().isPrimaryKeyColumn());
    		}
    	}

    protected void testPrimaryKey(PrimaryKey pk) {
    	assertNotNull(pk);
    	assertNotNull(pk.getTable());				
    	assertNotNull(pk.getColumnMap());
    	assertFalse(pk.getColumnMap().isEmpty());
    }

    public CatalogFactory factory() {
        // ClassLoader cl = getClassLoaderForGenerated();
        return getEnvironmentContext().getImplementation().catalogFactory();        
    }
    
    public Catalog getCatalog() throws QueryException, SQLException {
        Connection c = getConnection();
        CatalogFactory cf = factory();
        Catalog cat = cf.create(c);
        return cat;
    }
    
    public BaseTable getLanguageTable(Catalog cat) throws QueryException, SQLException {
    	return getWellKnownBaseTable(cat, SCHEMA_PUBLIC, TABLE_LANGUAGE);
    }

    protected BaseTable getFilmTable(Catalog cat) throws QueryException, SQLException {
    	return getWellKnownBaseTable(cat, SCHEMA_PUBLIC, TABLE_FILM);
    }

    protected BaseTable getWellKnownBaseTable(Catalog cat, String schema, String table)
        throws QueryException, SQLException {
        	SchemaMap sm = cat.schemas();
        	assertNotNull(sm);				
        	Schema pub = sm.get(schema);
        	assertNotNull(sm);
        	BaseTable t = pub.baseTables().get(table);
        	assertNotNull(t);		
        	return t;
        }

    protected DefaultTableMapper getTableMapper() {
        if (this.tableMapper == null) {
            this.tableMapper = new DefaultTableMapper(getRootPackage());
        }
        
        return this.tableMapper;
    }


    public String getRootPackage() {
        return "com.appspot.relaxe.gen";
    }
        
    protected File getGeneratedSrcDir() {
        return new File("out/src");
    }
    
    protected File getGeneratedBinDir() {
        return new File("out/bin");
    }
    
    @SuppressWarnings("deprecation")
    protected ClassLoader createClassLoaderForGenerated() 
        throws MalformedURLException {
        File dir = getGeneratedBinDir();
        URL[] path = { dir.toURL() };                  
        URLClassLoader gcl = new URLClassLoader(path);        
        return gcl;
    }
    
    protected ClassLoader getClassLoaderForGenerated() 
        throws MalformedURLException {
        if (this.classLoaderForGenerated == null) {
            this.classLoaderForGenerated = createClassLoaderForGenerated();            
        }

        return this.classLoaderForGenerated;
    }

	@Override
	public TestContext<I> getTestContext(I imp) throws SQLException, QueryException {
		if (testContext == null) {
			testContext = createTestContext();			
		}

		return testContext;
	}



	protected DefaultTestContext<I> createTestContext() throws SQLException,
			QueryException {
		return new DefaultTestContext<I>(getPersistenceContext(), getDatabase(), getJdbcConfig());
	}

	protected Properties getJdbcConfig() {
		Properties defaults = new Properties();
		defaults.setProperty("user", getUsername());
		defaults.setProperty("password", getPassword());
		return defaults;
	}


	public void setTestContext(TestContext<I> testContext) {
		this.testContext = testContext;
	}

	public <
		T extends ReferenceType<?, ?, T, E, ?, ?, ?, ?>,
		E extends Entity<?, ?, T, E, ?, ?, ?, ?>
	> 
	E newEntity(T type) {
		return type.getMetaData().getFactory().newEntity();
	}			
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;

import fi.tnie.db.DBMetaTest;
import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.EnvironmentTestContext;
import fi.tnie.db.QueryException;
import fi.tnie.db.QueryHelper;
import fi.tnie.db.build.Builder;
import fi.tnie.db.build.BuilderTest;
import fi.tnie.db.meta.impl.DefaultCatalogFactory;
import fi.tnie.db.meta.impl.mysql.MySQLCatalogFactory;
import fi.tnie.db.meta.impl.mysql.MySQLEnvironment;
import junit.framework.TestCase;

public class DBMetaTestCase
    extends TestCase
    implements DBMetaTest {
    
    public static final String SCHEMA_PUBLIC = "public";
    public static final String TABLE_CONTINENT = "continent";
    public static final String TABLE_COUNTRY = "country";
    
    private static Logger logger = Logger.getLogger(DBMetaTestCase.class);
    private EnvironmentTestContext context = null;
    
    private Connection connection = null;    
    private DefaultTableMapper tableMapper;
    
    private ClassLoader classLoaderForGenerated = null;
    
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
    	
    	{
    		Catalog catalog = cf.create(c);						
    		assertNotNull(catalog);
    	}
    	
    	CatalogMap cm = cf.createAll(c);
    	assertNotNull(cm);
    	
    	final String catalogName = cf.getCatalogName(c);
    	    	    	
    	Catalog catalog = cm.get(catalogName);						
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
//      
//      if (sp == null) {
//          // MySQL test database does not currently contain the schema public  
//          sp = sm.get(c.getCatalog());
//      }
        
        assertNotNull(sp);
        
        SchemaElementMap<? extends Table> tables = sp.tables();     
        assertNotNull(tables);
        assertNotNull(tables.values());
        
        BaseTable cont = getWellKnownBaseTable(catalog, SCHEMA_PUBLIC, TABLE_CONTINENT);
        assertNotNull(cont);

        BaseTable countries = getWellKnownBaseTable(catalog, SCHEMA_PUBLIC, TABLE_COUNTRY);
        assertNotNull(countries);

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
    public static Logger logger() {
        return DBMetaTestCase.logger;
    }

    @Override
    public void init(EnvironmentTestContext ctx) {
        
        logger().debug("\n\ninit()");
        Driver drv = ctx.getDriver();
        logger().debug(drv.getClass());
        logger().debug(drv.getMajorVersion() + "." + drv.getMinorVersion());
        logger().debug("\n\n");
        this.context = ctx;
    }

    public EnvironmentTestContext getContext() {
        logger().debug("getContext: " + this.context + " for " + id());
        return context;
    }
    
    
    public String id() {
        return getClass().getName() + "@" + System.identityHashCode(this);
    }
    
    private Connection connect() 
        throws SQLException {
        assertNotNull(this.context);
        Connection c = this.context.connect();
        assertNotNull(c);
        return c;
    }
    
    
    @Override
    protected void setUp() throws Exception {        
        super.setUp();
        this.connection = connect();
        this.connection.setAutoCommit(false);
    }
    
    @Override
    protected void tearDown() throws Exception {
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
    		assertFalse(fk.columns().isEmpty());
    		
    		for (Map.Entry<Column, Column> p : fk.columns().entrySet()) {					
    			assertNotNull(p.getKey());
    			assertNotNull(p.getValue());
    			
    			assertNotSame(p.getKey(), p.getValue());
    			
    			logger().debug("ref'ing: " + p.getKey().getUnqualifiedName().getName());
    			logger().debug("ref'ed: " + p.getValue().getUnqualifiedName().getName());
    			
    			assertNotNull(fk.getReferencing().columnMap().get(p.getKey().getUnqualifiedName()));
    			assertNotNull(fk.getReferenced().columnMap().get(p.getValue().getUnqualifiedName()));
    								
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
    	assertNotNull(pk.columns());
    	assertFalse(pk.columns().isEmpty());
    }

    public CatalogFactory factory() {
        // ClassLoader cl = getClassLoaderForGenerated();
        return getContext().getEnvironment().catalogFactory();        
    }
    
    public Catalog getCatalog() throws QueryException, SQLException {
        Connection c = getConnection();
        CatalogFactory cf = factory();
        Catalog cat = cf.create(c);
        return cat;
    }
    
    public BaseTable getContinentTable(Catalog cat) throws QueryException, SQLException {
    	return getWellKnownBaseTable(cat, SCHEMA_PUBLIC, TABLE_CONTINENT);
    }

    protected BaseTable getCountryTable(Catalog cat) throws QueryException, SQLException {
    	return getWellKnownBaseTable(cat, SCHEMA_PUBLIC, TABLE_COUNTRY);
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
        return DBMetaTestCase.class.getPackage().getName();
    }
        
    protected File getGeneratedSrcDir() {
        return new File("gen/src");
    }
    
    protected File getGeneratedBinDir() {
        return new File("gen/bin");
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
}

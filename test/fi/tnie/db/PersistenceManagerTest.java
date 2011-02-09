/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;

import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.DefaultPerson;
import fi.tnie.db.gen.ent.personal.PersonalFactory;
import fi.tnie.db.map.TableMapper;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.DBMetaTestCase;

public class PersistenceManagerTest extends DBMetaTestCase  {
	
	@Override
	protected void setUp() throws Exception {
	
//		PGImplementation impl = new PGImplementation();		
//		new SimpleTestContext(impl, driver, jdbcURL, driverConfig)		
//		init(ctx)		
//		super.setUp();
	}

    public void testPersistenceManager() 
        throws Exception {
        
        Connection c = getConnection();
        assertFalse(c.getAutoCommit());
        
        Catalog cat = getCatalog();
        
        testCatalog(cat, c);        
        TableMapper tm = getTableMapper();
        
        // ClassLoader gcl = createClassLoaderForGenerated();
        ClassLoader gcl = null;
        
        LiteralCatalog cc = LiteralCatalog.getInstance();
                
//        CatalogContext cc = new CatalogContext(cat, gcl, tm);
                        
//        assertEquals(cat, cc.boundTo());                
//        assertFalse(cc.getMetaMap().isEmpty());
                       
        BaseTable ct = getContinentTable(cat);
        assertNotNull(ct);
        
//        logger().debug("bound tables: " + cc.getMetaMap().keySet());
//        logger().debug("key-table: " + ct);
//        logger().debug("key-table-loader: " + ct.getClass().getClassLoader());
//        
//        logger().debug("bound-table-loader: " + cc.getMetaMap().keySet().iterator().next().getClass().getClassLoader());
                
//        assertTrue(cc.getMetaMap().containsKey(ct));               
                        
        EntityMetaData<?, ?, ?, ?> meta = cc.getMetaData(ct);
        assertNotNull(meta);
                
        EntityFactory<?, ?, ?, ?> ef = meta.getFactory();
        assertNotNull(ef);
                
        PersonalFactory pf = cc.newPersonalFactory();
        DefaultPerson p = pf.newPerson();
                
//        PersistenceManager<Attribute, Reference, Query, 
//        	Entity<Attribute, Reference, Query, Person.Type, ? extends Person>> pm = p.createPersistentManager();
                
        // p.setId(8);
        // p.setName("asdf");
        p.setFirstName("a");
        p.setLastName("b");
                                       
//        pm.merge(c);
//        c.commit();        
//        pm.delete(c);
//        c.commit();
//        pm.insert(c);
//        c.commit();
//        pm.update(c);
//        c.commit();
//        pm.delete(c);
//        c.commit();
        
//        DefaultContinent newCont = pf.newAbstractContinent();
//        newCont.setName("asdf");
//        pm = newCont.createPersistentManager();
//        pm.merge(c);
//        c.commit();
    }

}

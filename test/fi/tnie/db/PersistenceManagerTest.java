/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Set;

import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.ddl.SQLType;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.build.Builder;
import fi.tnie.db.build.BuilderTest;
import fi.tnie.db.build.CatalogContext;
import fi.tnie.db.build.pub.AbstractContinent;
import fi.tnie.db.build.pub.Continent;
import fi.tnie.db.build.pub.ContinentImpl;
import fi.tnie.db.build.pub.DefaultContinent;
import fi.tnie.db.build.pub.PublicFactory;
import fi.tnie.db.build.pub.Continent.Attribute;
import fi.tnie.db.build.pub.Continent.Query;
import fi.tnie.db.build.pub.Continent.Reference;

public class PersistenceManagerTest extends DBMetaTestCase  {

    public void testPersistenceManager() 
        throws Exception {
        
        Connection c = getConnection();
        assertFalse(c.getAutoCommit());
        
        Catalog cat = getCatalog();
        
        testCatalog(cat, c);        
        TableMapper tm = getTableMapper();
        
        // ClassLoader gcl = createClassLoaderForGenerated();
        ClassLoader gcl = null;
        
        CatalogContext cc = new CatalogContext(cat, gcl, tm);
                        
        assertEquals(cat, cc.boundTo());                
        assertFalse(cc.getMetaMap().isEmpty());
                       
        BaseTable ct = getContinentTable(cat);
        assertNotNull(ct);
        
        logger().debug("bound tables: " + cc.getMetaMap().keySet());
        logger().debug("key-table: " + ct);
        logger().debug("key-table-loader: " + ct.getClass().getClassLoader());
        
        logger().debug("bound-table-loader: " + cc.getMetaMap().keySet().iterator().next().getClass().getClassLoader());
        
        assertTrue(cc.getMetaMap().containsKey(ct));               
                        
        EntityMetaData<?, ?, ?, ?> meta = cc.getMetaData(ct);
        assertNotNull(meta);
                
        EntityFactory<?, ?, ?, ?> ef = meta.getFactory();
        assertNotNull(ef);
                
        PublicFactory pf = cc.newPublicFactory();
        DefaultContinent cont = pf.newAbstractContinent();
        
        PersistenceManager<Attribute, Reference, Query, Entity<Attribute, Reference, Query, ? extends Continent>> pm = cont.createPersistentManager();
        
        cont.setId(8);
        cont.setName("asdf");
               
        pm.merge(c);
        c.commit();        
        pm.delete(c);
        c.commit();
        pm.insert(c);
        c.commit();
        pm.update(c);
        c.commit();
        pm.delete(c);
        c.commit();        
    }

}

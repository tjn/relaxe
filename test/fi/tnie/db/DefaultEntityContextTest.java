/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.net.MalformedURLException;
import java.sql.SQLException;

import fi.tnie.db.ent.EntityException;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.map.TableMapper;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.pg.genctx.CatalogContext;
import fi.tnie.db.query.QueryException;

public abstract class DefaultEntityContextTest<I extends Implementation<I>> extends DBMetaTestCase<I> {

    public void testDefaultEntityContextCatalog() 
        throws EntityException, QueryException, SQLException, MalformedURLException {
        Catalog cat = getCatalog();        
        
        DefaultEntityContext cc = new CatalogContext(cat, null);
        
//        generated CatalogContext instance should be automatically 
//        bound by using DefaultTableMapper with same root-package name
//        used when generating
                
        assertNotNull(cc.boundTo()); 
        
        DefaultTableMapper tm = getTableMapper();        
        cc.bindAll(tm);        
        assertEquals(cat, cc.boundTo());
    }

    public void testDefaultEntityContextCatalogTableMapper() throws EntityException, QueryException, SQLException, MalformedURLException {
        Catalog cat = getCatalog();
        
        TableMapper tm = getTableMapper();
        CatalogContext cc = new CatalogContext(cat, null, tm);
        assertEquals(cat, cc.boundTo());        
    }

}

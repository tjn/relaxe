/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityContext;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.map.TableMapper;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.impl.pg.PGJDBCTestCase;
import fi.tnie.db.meta.impl.pg.PGTestCase;

public class PGDefaultEntityContextTest extends PGJDBCTestCase {
	
	public void testBind() throws Exception {
		
		Catalog catalog = getCatalog();
		
		TableMapper tm = new DefaultTableMapper("fi.tnie.db.generated");		
		final DefaultEntityContext ec = new DefaultEntityContext(catalog, null);
		ec.bindAll(tm);
								
		testBaseTable(getContinentTable(), ec);
		testBaseTable(getCountryTable(), ec);
				
	}

	protected void testBaseTable(BaseTable t, EntityContext ec) 
		throws EntityException {
						
		EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> md = ec.getMetaData(t);
		
		assertNotNull(md);
		assertNotNull(md.getBaseTable());
		assertSame(md.getBaseTable(), t);		
		assertNotNull(md.attributes());
		
		logger().debug("attributes: " + md.attributes());
		logger().debug("relationships: " + md.relationships());
				
		assertNotNull(md.getFactory());						
		Entity<?,?,?,?,?,?,?,?> e = md.getFactory().newInstance();
		assertNotNull(e);
		assertNotNull(e.getMetaData());		
		assertSame(md, e.getMetaData());
		
		
		
	}
	
	
}


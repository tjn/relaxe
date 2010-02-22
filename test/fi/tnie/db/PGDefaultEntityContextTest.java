/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.SQLException;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.impl.pg.PGTestCase;
import fi.tnie.db.meta.util.CatalogTraversal;

public class PGDefaultEntityContextTest extends PGTestCase {

	
	void testBind() throws Exception {
		
		Catalog catalog = getCatalog();
		
		TableMapper tm = new DefaultTableMapper("fi.tnie.db.generated");		
		final DefaultEntityContext ec = new DefaultEntityContext(catalog, tm);
		
		new CatalogTraversal() {
			@Override
				public boolean visitBaseTable(BaseTable t) {
					testBaseTable(t, ec);
					return false;
				}			
		}.traverse(catalog);		
		
	}

	protected void testBaseTable(BaseTable t, EntityContext ec) {		
		EntityMetaData<?, ?, ?, ?> md = ec.getMetaData(t);
		assertNotNull(md);
		assertNotNull(md.getBaseTable());
		assertSame(md.getBaseTable(), t);
				
//		md.getFactory().newInstance(ec.getMetaData(t));
		
	}
	
	
}


/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;

public class PagilaCreateCatalogTest 
	extends AbstractPagilaTestCase {
	
	public void testCreateCatalog() throws Exception {
	
		TestContext<PGImplementation> tc = getCurrent();
		Catalog cat = tc.getCatalog();
		
		assertNotNull(cat);
		
//		TestContext<PGImplementation> tc = getCurrent();
//		Connection conn = tc.newConnection();
//		
//		
//		PersistenceContext<PGImplementation> pc = tc.getPersistenceContext();
//		CatalogFactory cf = pc.getImplementation().catalogFactory();
//		Catalog cat = cf.create(conn);
		
		Schema ps = cat.schemas().get("public");
		assertNotNull(ps);
		BaseTable t = ps.baseTables().get("film");
		assertNotNull(t);
		Column c = t.columnMap().get("title");
		assertNotNull(c);
		
		assertNotNull(c.getColumnName());
		DataType type = c.getDataType();
		assertNotNull(type);
		
		Integer colen = type.getCharOctetLength();
		assertNotNull(colen);
		assertEquals(255, colen.intValue());
	}
	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.expr.ddl.AlterTableAddPrimaryKey;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;

public class PagilaAlterTableAddPrimaryKeyTest
	extends AbstractPagilaTestCase {
		
	public void testAlterTablePK() {		
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		BaseTable t = meta.getBaseTable();
		
		PrimaryKey pk = t.getPrimaryKey();
		
		assertNotNull(pk);
		
		assertNotNull(pk.getUnqualifiedName());
		
		AlterTableAddPrimaryKey pkdef = new AlterTableAddPrimaryKey(pk);
		String result = pkdef.generate();
				
		logger().debug("result: " + result);
		
		assertNotNull(result);
		
		assertTrue(result.matches("ALTER TABLE +public.film +ADD +CONSTRAINT +film_pkey +PRIMARY KEY *\\(film_id\\) *"));
	}

}

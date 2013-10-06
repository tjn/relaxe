/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.expr.ddl.AlterTableAddForeignKey;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;

public class AlterTableAddForeignKeyTest
	extends AbstractPagilaTestCase {
		
	public void testAlterTableFK() {		
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		BaseTable t = meta.getBaseTable();
		
		ForeignKey fk = t.foreignKeys().get(Film.Reference.LANGUAGE.identifier());
		assertNotNull(fk);
		
		AlterTableAddForeignKey at = new AlterTableAddForeignKey(fk);
		String result = at.generate();
		assertNotNull(result);
		
		assertTrue(result.matches("ALTER TABLE +public.film +ADD +CONSTRAINT +film_language_id_fkey +FOREIGN KEY *\\(language_id\\) *REFERENCES +public.language *\\(language_id\\)"));
	}

}

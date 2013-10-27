/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.sakila;

import com.appspot.relaxe.expr.ddl.AlterTableAddForeignKey;
import com.appspot.relaxe.gen.mysql.sakila.ent.sakila.Film;
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
		
		logger().debug("result: " + result);
		
// 		assertTrue(result.matches("ALTER TABLE\"sakila\"."film"ADD CONSTRAINT"FK_film_language"FOREIGN KEY("language_id")REFERENCES"sakila"."language"("language_id")"));
	}

}

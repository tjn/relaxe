/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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

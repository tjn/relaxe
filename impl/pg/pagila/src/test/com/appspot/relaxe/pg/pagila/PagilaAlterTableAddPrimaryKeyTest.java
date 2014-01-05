/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
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
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.expr.ddl.AlterTableAddPrimaryKey;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
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

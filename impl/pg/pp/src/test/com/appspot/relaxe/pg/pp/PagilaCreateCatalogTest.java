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
package com.appspot.relaxe.pg.pp;

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

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
package com.appspot.relaxe.pg.pp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.env.pg.PGEnvironment;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.gen.pg.pp.ent.pub.Film;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.types.PrimitiveType;

import junit.framework.TestCase;

public class PagilaCreateTableTest extends TestCase {

	private static Logger logger = LoggerFactory.getLogger(PagilaCreateTableTest.class);

	public void testCreateTable() {

		BaseTable table = Film.Type.TYPE.getMetaData().getBaseTable();

		{
			SchemaElementName sen = table.getName();
			assertNotNull(sen);
			SchemaName q = sen.getQualifier();
			assertNotNull(q);
			assertNotNull(q.getSchemaName());
			assertNull(q.getCatalogName());
		}

		{
			SchemaElementName sen = table.getPrimaryKey().getName();
			assertNotNull(sen);
			SchemaName q = sen.getQualifier();
			assertNotNull(q);
			assertNotNull(q.getSchemaName());
			assertNull(q.getCatalogName());
		}


		{
			Column title = table.columnMap().get("TITLE");
			assertNotNull(title);
			DataType type = title.getDataType();
			assertNotNull(type);
			logger.debug("char-octet-length: " + type.getCharOctetLength());
			assertNotNull(type.getCharOctetLength());
			assertEquals(255, type.getCharOctetLength().intValue());
		}


		{
			Column desc = table.columnMap().get("DESCRIPTION");
			assertNotNull(desc);
			DataType type = desc.getDataType();
			assertNotNull(type);
			assertTrue("text".equalsIgnoreCase(type.getTypeName()));

			logger.debug("size: " + type.getSize());
			logger.debug("char-octet-length: " + type.getCharOctetLength());
		}

		{
			Column desc = table.columnMap().get("special_features");
			assertNotNull(desc);
			DataType type = desc.getDataType();
			assertNotNull(type);
			assertEquals(PrimitiveType.ARRAY, type.getDataType());
			assertTrue("_text".equalsIgnoreCase(type.getTypeName()));


			DataTypeMap dm = PGEnvironment.environment().getDataTypeMap();
			SQLTypeDefinition def = dm.getSQLTypeDefinition(type);
			assertNotNull(def);
			logger.debug(def.toString());
			assertTrue(def instanceof SQLArrayTypeDefinition);
			SQLArrayTypeDefinition adef = (SQLArrayTypeDefinition) def;
			SQLTypeDefinition edef = adef.getElementType();
			logger.debug(edef.toString());
		}

		CreateTable ct = new CreateTable(table);
		String ddl = ct.generate();

		logger().debug("ddl: " + ddl);
	}


	private static Logger logger() {
		return PagilaCreateTableTest.logger;
	}
}

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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import com.appspot.relaxe.env.util.ResultSetWriter;
import com.appspot.relaxe.mysql.sakila.AbstractMySQLSakilaTestCase;


public class SakilaForeignKeyTest
	extends AbstractMySQLSakilaTestCase {
		
	public void testForeignKeys() throws Exception {
		Connection c = getContext().newConnection();		
		DatabaseMetaData meta = c.getMetaData();
		
		logger().debug("testPrimaryKeys: meta.getURL()=" + meta.getURL());
		
		{
			ResultSet rs = meta.getTables(null, null, "%", new String[] {"TABLE"} );
			ResultSetWriter w = new ResultSetWriter(System.out, false);
			w.apply(rs, true);
			logger().debug("tables: count: " + w.processed());
		}
		
		{		
			ResultSet rs = meta.getImportedKeys(getDatabase(), null, "film");		
			ResultSetWriter w = new ResultSetWriter(System.out, false);
			w.apply(rs, true);			
			logger().debug("meta.getImportedKeys: count: " + w.processed());
		}				
		
	}
}

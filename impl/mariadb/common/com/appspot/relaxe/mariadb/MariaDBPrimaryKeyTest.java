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
package com.appspot.relaxe.mariadb;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class MariaDBPrimaryKeyTest
	extends AbstractMariaDBTestCase {
		
	public void testPrimaryKey() throws Exception {
		Connection c = getContext().newConnection();		
		DatabaseMetaData meta = c.getMetaData();
		
		
		logger().debug("c.class=" + c.getClass());
		
		logger().debug("testPrimaryKeys: meta.getURL()=" + meta.getURL());		
		logger().debug("testPrimaryKey: getDatabase()=" + getDatabase());
		logger().debug("testPrimaryKey: meta.getDriverVersion()=" + meta.getDriverVersion());
		logger().debug("testPrimaryKey: meta.getDriverMinorVersion()=" + meta.getDriverMinorVersion());
		
		
		{		
			ResultSet rs = meta.getPrimaryKeys(getDatabase(), getDatabase(), "film_actor");
			int count = 0;
			while(rs.next()) {
				count++;
			}
			
			logger().debug("testPrimaryKey: count=" + count);
			
//			ResultSetWriter w = new ResultSetWriter(System.out, false);
//			w.apply(rs, true);			
//			logger().debug("meta.getPrimaryKeys: count: " + w.processed());
		}
	}	
	
	@Override
	public String getHost() {
		return "127.0.0.1";
	}

	
	@Override
	public String getDatabase() {
		return "sakila";
	}
}

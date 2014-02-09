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
package com.appspot.relaxe.mysql.samples;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.rdbms.mysql.MySQLImplementation;
import com.appspot.relaxe.rdbms.util.ResultSetWriter;
import com.appspot.relaxe.mysql.AbstractMySQLTestCase;
import com.appspot.relaxe.query.QueryException;


public class MySQLSamplesMetaTest
	extends AbstractMySQLTestCase {
	
	@Override
	protected PersistenceContext<MySQLImplementation> createPersistenceContext() {
		return new MySQLSamplesTriggerTestPersistenceContext();
	}
		
	@Override
	public String getDatabase() {
		return "samples";
	}	
	
	@Override
	public String getHost() {
		return "127.0.0.1";
	}
	
	
	public void testPrimaryKeys() throws SQLException, ClassNotFoundException, IOException, QueryException {
		
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
			ResultSet rs = meta.getPrimaryKeys("samples", null, "abc");		
			ResultSetWriter w = new ResultSetWriter(System.out, false);
			w.apply(rs, true);			
			logger().debug("testPrimaryKeys: count: " + w.processed());
		}		
	}

}

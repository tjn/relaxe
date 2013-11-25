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
package com.appspot.relaxe.mariadb.sakila;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

import com.appspot.relaxe.mariadb.AbstractMariaDBTestCase;



public class MariaDBTestGeneratedKeys
	extends AbstractMariaDBTestCase {
	
	public void testGeneratedKeys1() throws Exception {				

		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			c = newConnection();
			DatabaseMetaData meta = c.getMetaData();
			
			logger().debug("testGeneratedKeys1: meta.getURL()=" + meta.getURL());
			
			st = c.createStatement();
			boolean result = st.execute("insert into film(film_id, title, language_id) values (default, 'Avatar', 1)", Statement.RETURN_GENERATED_KEYS);
						
			logger().debug("testGeneratedKeys: result=" + result);
			
			rs = st.getGeneratedKeys();
			
			logger().debug("testGeneratedKeys: rs=" + rs);
			
			if (rs != null) {
				logger().debug("testGeneratedKeys: meta-data: ");				
				dump(rs.getMetaData());
				
				int kc = 0;
				
				while(rs.next()) {
					kc++;
				}
				
				logger().debug("testGeneratedKeys1: kc=" + kc);				
			}
						
			c.commit();
		}
		catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
		finally {
			rs = close(rs);
			close(st);
			close(c);
		}
	}
	
	public void _testGeneratedKeys2() throws Exception {				

		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			c = newConnection();
			st = c.createStatement();
						
			int u = st.executeUpdate("insert into film(film_id, title, language_id) values (default, 'Avatar', 1)", new int[] { 1, 2 });
			
			logger().debug("testGeneratedKeys2: u=" + u);
			
			rs = st.getGeneratedKeys();
			
			logger().debug("testGeneratedKeys2: rs=" + rs);
			
			if (rs != null) {
				logger().debug("testGeneratedKeys: meta-data: ");				
				dump(rs.getMetaData());
				
				int kc = 0;
				
				while(rs.next()) {
					kc++;
				}
				
				logger().debug("testGeneratedKeys1: kc=" + kc);				
			}
			
//			String q = "SELECT * FROM diagnostic_test.mykt";
//			dumpMetaDataWithSamples(st, q);
			
			
		}
		catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
		finally {
			rs = close(rs);
			close(st);
			close(c);
		}
	}
	
	public void testGeneratedKeys3() throws Exception {				

		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			c = newConnection();
			st = c.createStatement();
						
			int u = st.executeUpdate("insert into film(film_id, title, language_id) values (default, 'Avatar', 1)", new String[] { "film_id", "title" });
			
			logger().debug("testGeneratedKeys: result=" + u);
			
			rs = st.getGeneratedKeys();
			
			logger().debug("testGeneratedKeys: rs=" + rs);
			
			if (rs != null) {
				logger().debug("testGeneratedKeys: meta-data: ");				
				dump(rs.getMetaData());
				
				int kc = 0;
				
				while(rs.next()) {
					kc++;
				}
				
				logger().debug("testGeneratedKeys1: kc=" + kc);				
			}	
		}
		catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
		finally {
			rs = close(rs);
			close(st);
			close(c);
		}
	}

	@Override
	public String getDatabase() {
		return "sakila";
	}	
}

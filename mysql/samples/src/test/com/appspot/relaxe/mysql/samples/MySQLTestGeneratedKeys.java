/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.samples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.appspot.relaxe.mysql.AbstractMySQLTestCase;



public class MySQLTestGeneratedKeys
	extends AbstractMySQLTestCase {
	
	public void testGeneratedKeys1() throws Exception {				

		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			c = newConnection();
			st = c.createStatement();
			boolean result = st.execute("insert into diagnostic_test.abc(id, name) values(DEFAULT, 'topi')", Statement.RETURN_GENERATED_KEYS);
						
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
	
	public void testGeneratedKeys2() throws Exception {				

		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			c = newConnection();
			st = c.createStatement();
						
			int u = st.executeUpdate("insert into diagnostic_test.abc(id, name) values(DEFAULT, 'topi')", new int[] { 1, 2 });
			
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
			
			String q = "SELECT * FROM diagnostic_test.mykt";
			dumpMetaDataWithSamples(st, q);
			
			
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
						
			int u = st.executeUpdate("insert into diagnostic_test.abc(id, name) values(DEFAULT, 'topi')", new String[] { "id", "name" });
			
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
		return null;
	}
	
}

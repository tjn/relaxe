/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.samples;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import com.appspot.relaxe.mysql.AbstractMySQLTestCase;



public class MySQLTestKeywords
	extends AbstractMySQLTestCase {
	
	@Override
	public String getDatabase() {
		return "samples";
	}
	
	public void testKeywords() throws Exception {				

		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			c = newConnection();
			
			DatabaseMetaData meta = c.getMetaData();
			String kwlist = meta.getSQLKeywords();
			logger().debug("testKeywords: kwlist=" + kwlist);
			
			Arrays.asList(kwlist.split(","));
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
}

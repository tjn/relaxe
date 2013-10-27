/*
 * Copyright (c) 2009-2013 Topi Nieminen
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

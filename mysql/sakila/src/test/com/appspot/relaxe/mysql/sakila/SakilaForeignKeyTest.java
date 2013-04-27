/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.sakila;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import com.appspot.relaxe.env.util.ResultSetWriter;
import com.appspot.relaxe.mysql.sakila.SakilaTestCase;


public class SakilaForeignKeyTest
	extends SakilaTestCase {
		
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

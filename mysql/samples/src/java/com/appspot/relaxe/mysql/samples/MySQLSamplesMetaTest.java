/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.samples;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.mysql.MySQLImplementation;
import com.appspot.relaxe.env.util.ResultSetWriter;
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

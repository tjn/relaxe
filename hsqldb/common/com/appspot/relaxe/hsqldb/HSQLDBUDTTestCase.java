/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.hsqldb;

import java.io.BufferedOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Types;
import com.appspot.relaxe.env.util.ResultSetWriter;


public class HSQLDBUDTTestCase
	extends AbstractHSQLDBTestCase {

	

	@Override
	public String getDatabase() {
		return "default";
	}


	public void testDump() throws Exception {
		Connection c = getCurrent().newConnection();
	
		DatabaseMetaData meta = c.getMetaData();
		
		logger().debug("testDump: meta.getURL()=" + meta.getURL());
		
		BufferedOutputStream out = new BufferedOutputStream(System.out, 64 * 1024);
		
		com.appspot.relaxe.env.util.ResultSetWriter rw = new ResultSetWriter(out, false);		
		
		rw.header("JavaType Info");
		rw.apply(meta.getTypeInfo());

		rw.header("UDTs");
		rw.apply(meta.getUDTs(null, "public", null, new int[] { Types.DISTINCT}));
		
	}
	
}

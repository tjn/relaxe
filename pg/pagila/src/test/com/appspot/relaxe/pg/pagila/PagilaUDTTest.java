/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;


public class PagilaUDTTest 
	extends AbstractPagilaTestCase {

	public void testLoad() throws Exception {		
		logger().debug("testLoad - enter");
		
		TestContext<PGImplementation> ts = getCurrent();
		Connection c = ts.newConnection();
		
		DatabaseMetaData meta = c.getMetaData();
		
		Statement st = c.createStatement();
		
		ResultSet rs = st.executeQuery("select a2_8 from public.default_test");
		
		if (rs.next()) {
			Array array = rs.getArray(1);
			
			logger().debug("baseType: " + array.getBaseType());
			String baseTypeName = array.getBaseTypeName();			
			logger().debug("baseTypeName: " + baseTypeName);			
		}
		
		logger().debug("testLoad - exit");
	}

}

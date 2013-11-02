/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.io.BufferedOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.appspot.relaxe.env.util.ResultSetWriter;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;

public class PagilaTypeTest extends AbstractPagilaTestCase {
	
	private static Logger logger = Logger.getLogger(PagilaTypeTest.class);

	public void testDistinctTypes() throws Exception {
		Connection c = getCurrent().newConnection();	
		DatabaseMetaData meta = c.getMetaData();		
		BufferedOutputStream out = new BufferedOutputStream(System.out, 64 * 1024);		
		com.appspot.relaxe.env.util.ResultSetWriter rw = new ResultSetWriter(out, false);		
		
//		rw.header("JavaType Info");
//		rw.apply(meta.getTypeInfo());

		rw.header("Distinct types");
		rw.apply(meta.getUDTs(null, "public", null, new int[] { Types.DISTINCT}));
	}
	
	public void testUDTs() throws Exception {
		Connection c = getCurrent().newConnection();	
		DatabaseMetaData meta = c.getMetaData();		
		BufferedOutputStream out = new BufferedOutputStream(System.out, 64 * 1024);		
		com.appspot.relaxe.env.util.ResultSetWriter rw = new ResultSetWriter(out, false);		
		
//		rw.header("JavaType Info");
//		rw.apply(meta.getTypeInfo());

		rw.header("UDTs");
		rw.apply(meta.getUDTs(null, "public", null, null));
	}
	
	public void testTypeInfo() throws Exception {
		Connection c = getCurrent().newConnection();
	
		DatabaseMetaData meta = c.getMetaData();
		
		BufferedOutputStream out = new BufferedOutputStream(System.out, 64 * 1024);
		
		com.appspot.relaxe.env.util.ResultSetWriter rw = new ResultSetWriter(out, false);		
		
		rw.header("JavaType Info");
		rw.apply(meta.getTypeInfo());
	}	

	
	
}

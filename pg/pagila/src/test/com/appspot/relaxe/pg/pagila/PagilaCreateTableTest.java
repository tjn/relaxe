/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import org.apache.log4j.Logger;

import com.appspot.relaxe.env.pg.PGEnvironment;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.types.PrimitiveType;

import junit.framework.TestCase;

public class PagilaCreateTableTest extends TestCase {
	
	private static Logger logger = Logger.getLogger(PagilaCreateTableTest.class);

	public void testCreateTable() {
		
		BaseTable table = Film.Type.TYPE.getMetaData().getBaseTable();
				
		{
			Column title = table.columnMap().get("TITLE");
			assertNotNull(title);
			DataType type = title.getDataType();
			assertNotNull(type);
			logger.debug("char-octet-length: " + type.getCharOctetLength());		
			assertNotNull(type.getCharOctetLength());
			assertEquals(255, type.getCharOctetLength().intValue());
		}
						
		
		{
			Column desc = table.columnMap().get("DESCRIPTION");
			assertNotNull(desc);
			DataType type = desc.getDataType();
			assertNotNull(type);
			assertTrue("text".equalsIgnoreCase(type.getTypeName()));
			
			logger.debug("size: " + type.getSize());
			logger.debug("char-octet-length: " + type.getCharOctetLength());
		}
		
		{
			Column desc = table.columnMap().get("special_features");
			assertNotNull(desc);
			DataType type = desc.getDataType();
			assertNotNull(type);
			assertEquals(PrimitiveType.ARRAY, type.getDataType());			
			assertTrue("_text".equalsIgnoreCase(type.getTypeName()));
			
			
			DataTypeMap dm = PGEnvironment.environment().getDataTypeMap();
			SQLTypeDefinition def = dm.getSQLTypeDefinition(type);
			assertNotNull(def);
			logger.debug(def);
			assertTrue(def instanceof SQLArrayTypeDefinition);
			SQLArrayTypeDefinition adef = (SQLArrayTypeDefinition) def;
			SQLTypeDefinition edef = adef.getElementType();
			logger.debug(edef);
		}
		
		CreateTable ct = new CreateTable(table);		
		String ddl = ct.generate();
		
		logger().debug("ddl: " + ddl);
	}

	
	private static Logger logger() {
		return PagilaCreateTableTest.logger;
	}
}

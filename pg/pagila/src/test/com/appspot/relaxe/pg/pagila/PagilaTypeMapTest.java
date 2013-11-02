/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import org.apache.log4j.Logger;

import com.appspot.relaxe.env.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBPersistenceContext;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.ddl.types.AbstractCharacterTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarBinaryTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarcharTypeDefinition;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.DataTypeTest;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.impl.hsqldb.HSQLDBEnvironment;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.types.PrimitiveType;

public class PagilaTypeMapTest 
	extends AbstractPagilaTestCase {
	
	private static Logger logger = Logger.getLogger(PagilaTypeMapTest.class);	
	
	
	public void testTypeMap() throws Exception {
		
		HSQLDBImplementation hi = new HSQLDBImplementation();
		HSQLDBPersistenceContext hpc = new HSQLDBPersistenceContext(hi);		
		HSQLDBEnvironment henv = hi.getEnvironment();
		final DataTypeMap htm = henv.getDataTypeMap();
		final DataTypeMap dtm = new DataTypeMap() {			
			@Override
			public PrimitiveType<?> getType(DataType type) {
				return htm.getType(type);
			}
			
			@Override
			public SQLTypeDefinition getSQLTypeDefinition(DataType dataType) {
				SQLTypeDefinition def = htm.getSQLTypeDefinition(dataType);
				
				if (def == null) {
					int t = dataType.getDataType();
					
					logger.debug("unmapped: " + dataType.getTypeName() + ": " + dataType.getDataType());
					
					if (t == PrimitiveType.ARRAY && dataType.getTypeName().equals("_text")) {
						def = new SQLArrayTypeDefinition(VarcharTypeDefinition.get(null));
					}
					
					if (t == PrimitiveType.BINARY && dataType.getTypeName().equals("bytea")) {
						def = new SQLArrayTypeDefinition(VarBinaryTypeDefinition.get());
					}
					
					if (SQLTypeDefinition.isBinaryType(t)) {
						def = VarBinaryTypeDefinition.get(dataType.getSize());
					}
				}
				
				return def;
			}
		};
		
		{
			DataTypeTest.MetaData tm = DataTypeTest.Type.TYPE.getMetaData();
						
			Column col = tm.getColumn(DataTypeTest.Attribute.CV);			
			DataType t = col.getDataType();
			
			SQLTypeDefinition def = dtm.getSQLTypeDefinition(t);
			AbstractCharacterTypeDefinition cd = (AbstractCharacterTypeDefinition) def;
			IntLiteral len = cd.getLength();
			assertNotNull(len);
		}
	}
		
}

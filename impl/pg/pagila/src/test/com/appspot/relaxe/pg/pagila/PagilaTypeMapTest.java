/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.env.hsqldb.AbstractHSQLDBImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBMemImplementation;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;

public class PagilaTypeMapTest 
	extends AbstractPagilaTestCase {
	
//	private static Logger logger = LoggerFactory.getLogger(PagilaTypeMapTest.class);	
	
	public void testTypeMap() throws Exception {
		
//		AbstractHSQLDBImplementation hi = new HSQLDBMemImplementation();
//		HSQLDBEnvironment henv = hi.getEnvironment();
//
//		final DataTypeMap htm = henv.getDataTypeMap();
//		final DataTypeMap dtm = new DataTypeMap() {			
//			@Override
//			public ValueType<?> getType(DataType type) {
//				return htm.getType(type);
//			}
//			
//			@Override
//			public SQLTypeDefinition getSQLTypeDefinition(DataType dataType) {
//				SQLTypeDefinition def = htm.getSQLTypeDefinition(dataType);
//				
//				if (def == null) {
//					int t = dataType.getDataType();
//					
//					logger.debug("unmapped: " + dataType.getTypeName() + ": " + dataType.getDataType());
//					
//					if (t == ValueType.ARRAY && dataType.getTypeName().equals("_text")) {
//						def = new SQLArrayTypeDefinition(VarcharTypeDefinition.get(null));
//					}
//					
//					if (t == ValueType.BINARY && dataType.getTypeName().equals("bytea")) {
//						def = new SQLArrayTypeDefinition(VarBinaryTypeDefinition.get());
//					}
//					
//					if (SQLTypeDefinition.isBinaryType(t)) {
//						def = VarBinaryTypeDefinition.get(dataType.getSize());
//					}
//				}
//				
//				return def;
//			}
//		};
		
//		{
//			DataTypeTest.MetaData tm = DataTypeTest.Type.TYPE.getMetaData();
//						
//			Column col = tm.getColumn(DataTypeTest.Attribute.CV);			
//			DataType t = col.getDataType();
//			
//			SQLTypeDefinition def = dtm.getSQLTypeDefinition(t);
//			AbstractCharacterTypeDefinition cd = (AbstractCharacterTypeDefinition) def;
//			IntLiteral len = cd.getLength();
//			assertNotNull(len);
//		}
	}
		
}

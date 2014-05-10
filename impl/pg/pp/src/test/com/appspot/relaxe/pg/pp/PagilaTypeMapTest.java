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
package com.appspot.relaxe.pg.pp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.rdbms.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.ddl.types.AbstractSQLCharacterType;
import com.appspot.relaxe.expr.ddl.types.SQLVarBinaryType;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLPredefinedDataType;
import com.appspot.relaxe.expr.ddl.types.SQLVarcharType;
import com.appspot.relaxe.gen.pg.pp.ent.pub.DataTypeTest;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.env.hsqldb.HSQLDBEnvironment;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.types.PrimitiveType;

public class PagilaTypeMapTest
	extends AbstractPagilaTestCase {

	private static Logger logger = LoggerFactory.getLogger(PagilaTypeMapTest.class);


	public void testTypeMap() throws Exception {

		HSQLDBImplementation hi = new HSQLDBImplementation.Mem();
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

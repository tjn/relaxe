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
package com.appspot.relaxe.env.mariadb;

import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.types.SQLArrayType;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;
import com.appspot.relaxe.expr.ddl.types.UserDefinedType;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeImpl;
import com.appspot.relaxe.meta.DefaultDataTypeMap;
import com.appspot.relaxe.types.ValueType;

public class MariaDBDataTypeMap
	extends DefaultDataTypeMap {
	
	
	public MariaDBDataTypeMap() {
	}

	@Override
	public SQLDataType getSQLType(DataType dataType) {
		SQLDataType t = super.getSQLType(dataType);
				
		if (t == null) {
			switch (dataType.getDataType()) {
				case ValueType.DISTINCT:
				case ValueType.OTHER:
				{
					MariaDBIdentifierRules nr = MariaDBEnvironment.environment().getIdentifierRules();
					SchemaElementName name = nr.newName(dataType.getTypeName());
					t = new UserDefinedType(name);
					break;
				}
				case ValueType.ARRAY:
				{
					ValueType<?> pt = getType(dataType);
					
					if (pt != null) {
						com.appspot.relaxe.types.ArrayType<?, ?> at = pt.asArrayType();			
						ValueType<?> et = at.getElementType();			
						DataTypeImpl dti = new DataTypeImpl(et.getSqlType(), et.getName());
						SQLDataType idef = getSQLType(dti);
						t = new SQLArrayType(idef);
					}
					break;
				}
			}
		}
		
		return t;
	}
}
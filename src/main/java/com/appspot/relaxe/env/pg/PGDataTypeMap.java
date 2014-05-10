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
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.env.pg.expr.PGByteArrayTypeDefinition;
import com.appspot.relaxe.env.pg.expr.PGTextArrayTypeDefinition;
import com.appspot.relaxe.env.pg.expr.PGTextTypeDefinition;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DefaultDataTypeMap;
import com.appspot.relaxe.types.ValueType;

public class PGDataTypeMap
	extends DefaultDataTypeMap {
    
	protected PGDataTypeMap() {
	}
	
	@Override
	public SQLDataType getSQLType(DataType t) {
		
		if (SQLDataType.isTextType(t.getDataType()) && PGTextTypeDefinition.NAME.equals(t.getTypeName())) {
			return PGTextTypeDefinition.DEFINITION;
		}
		
		if (t.getDataType() == ValueType.ARRAY && PGTextArrayTypeDefinition.NAME.equals(t.getTypeName())) {
			return PGTextArrayTypeDefinition.DEFINITION;
		}
						
		if (SQLDataType.isBinaryType(t.getDataType()) && PGByteArrayTypeDefinition.NAME.equals(t.getTypeName())) {
			return PGByteArrayTypeDefinition.DEFINITION;
		}
		
		return super.getSQLType(t);
	}
	
	@Override
	public ValueType<?> getType(DataType type) {
		int t = type.getDataType();
		String n = type.getTypeName();
		
		if ((t == PGTextType.TYPE.getSqlType()) && PGTextType.TYPE.getName().equals(n)) {
			return PGTextType.TYPE;
		}
		
		ValueType<?> pt = super.getType(type);
		
		if (pt == null) {			
			switch (t) {
			case ValueType.VARCHAR:
				if (PGTextType.TYPE.getName().equals(n)) {
					pt = PGTextType.TYPE;
				}
				break;			
			case ValueType.ARRAY:
				if (PGTextArrayType.TYPE.getName().equals(n)) {
					pt = PGTextArrayType.TYPE;
				}
				break;
			case ValueType.OTHER:
				if (PGTSVectorType.TYPE.getName().equals(n)) {
					pt = PGTSVectorType.TYPE;
				}
				break;				
			default:
				break;
			}
		}
		
		return pt;
	}
	
	@Override
	protected Integer getSize(DataType dataType) {
		if (SQLDataType.isBinaryIntegerType(dataType.getDataType())) {
			return null;
		}		
		
		return super.getSize(dataType);
	}

	@Override
	public SchemaElementName newName(String typeName) {
		return PGEnvironment.environment().getIdentifierRules().newName(typeName);
	}
	
}

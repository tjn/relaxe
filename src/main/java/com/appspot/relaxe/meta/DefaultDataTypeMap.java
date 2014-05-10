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
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.BigIntTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLBooleanType;
import com.appspot.relaxe.expr.ddl.types.SQLDoublePrecisionType;
import com.appspot.relaxe.expr.ddl.types.SQLDateType;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;
import com.appspot.relaxe.expr.ddl.types.SQLCharType;
import com.appspot.relaxe.expr.ddl.types.SQLDecimalType;
import com.appspot.relaxe.expr.ddl.types.SQLFloatType;
import com.appspot.relaxe.expr.ddl.types.SQLIntType;
import com.appspot.relaxe.expr.ddl.types.SQLLongVarcharType;
import com.appspot.relaxe.expr.ddl.types.SQLNumericType;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLSmallIntType;
import com.appspot.relaxe.expr.ddl.types.SQLTimeType;
import com.appspot.relaxe.expr.ddl.types.SQLTimestampType;
import com.appspot.relaxe.expr.ddl.types.SQLTinyIntType;
import com.appspot.relaxe.expr.ddl.types.UserDefinedType;
import com.appspot.relaxe.expr.ddl.types.SQLVarcharType;
import com.appspot.relaxe.types.BooleanType;
import com.appspot.relaxe.types.CharType;
import com.appspot.relaxe.types.DateType;
import com.appspot.relaxe.types.DecimalType;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.LongVarcharType;
import com.appspot.relaxe.types.NumericType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.TimeType;
import com.appspot.relaxe.types.TimestampType;
import com.appspot.relaxe.types.VarcharType;

public abstract class DefaultDataTypeMap 
	implements DataTypeMap {

	public DefaultDataTypeMap() {
	}
	
	@Override
	public ValueType<?> getType(DataType type) {
		switch (type.getDataType()) {
			case ValueType.SMALLINT:
			case ValueType.TINYINT:
			case ValueType.INTEGER:
				return IntegerType.TYPE;
			case ValueType.BIGINT:
				return LongType.TYPE;	
			case ValueType.BOOLEAN:
				return BooleanType.TYPE;
			case ValueType.VARCHAR:
				return VarcharType.TYPE;
			case ValueType.LONGVARCHAR:
				return LongVarcharType.TYPE;
			case ValueType.CHAR:			
				return CharType.TYPE;
			case ValueType.NUMERIC:			
				return NumericType.TYPE;			
			case ValueType.DECIMAL:			
				return DecimalType.TYPE;
			case ValueType.TIMESTAMP:			
				return TimestampType.TYPE;
			case ValueType.DATE:			
				return DateType.TYPE;		
			case ValueType.TIME:			
				return TimeType.TYPE;
				
			// Implementation for the following three 
			case ValueType.DISTINCT:			
			case ValueType.OTHER:					
			case ValueType.ARRAY:					
			default:
				break;
		}
		
		return null;
	}
	
	@Override
	public SQLDataType getSQLType(DataType dataType) {
		int t = dataType.getDataType();
		Integer s = getSize(dataType);
		
		SQLDataType def = null;
						
		switch (t) {
		case ValueType.INTEGER:
			def = SQLIntType.get(null);
			break;
		case ValueType.BIGINT:
			def = BigIntTypeDefinition.get(null);	
			break;							
		case ValueType.SMALLINT:
			def = SQLSmallIntType.get(null);
			break;										
		case ValueType.TINYINT:
			def = SQLTinyIntType.get(null);
			break;
		case ValueType.VARCHAR:			
			def = SQLVarcharType.get(getCharOctetLength(dataType));
			break;
		case ValueType.LONGVARCHAR:
			def = SQLLongVarcharType.get(getCharOctetLength(dataType));
			break;
		case ValueType.CHAR:			
			def = SQLCharType.get(getCharOctetLength(dataType));
			break;
		case ValueType.NUMERIC:			
			def = SQLNumericType.get(s, Integer.valueOf(dataType.getDecimalDigits()));
			break;
		case ValueType.DECIMAL:			
			def = SQLDecimalType.get(s, Integer.valueOf(dataType.getDecimalDigits()));
			break;
		case ValueType.TIMESTAMP:			
			def = SQLTimestampType.get();
			break;
		case ValueType.DATE:			
			def = SQLDateType.get();
			break;
		case ValueType.TIME:			
			def = SQLTimeType.get();
			break;
		case ValueType.BOOLEAN:
			// fall-through
		case ValueType.BIT:			
			def = SQLBooleanType.get();
			break;
		case ValueType.DOUBLE:			
			def = SQLDoublePrecisionType.get();
			break;
		case ValueType.FLOAT:			
			def = SQLFloatType.get();
			break;			
		case ValueType.DISTINCT:			
		case ValueType.OTHER:
			SchemaElementName name = newName(dataType.getTypeName());
			def = new UserDefinedType(name);
			break;
		case ValueType.ARRAY:			
			ValueType<?> pt = getType(dataType);
			
			if (pt != null) {
				com.appspot.relaxe.types.ArrayType<?, ?> at = pt.asArrayType();			
				ValueType<?> et = at.getElementType();			
				DataTypeImpl dti = new DataTypeImpl(et.getSqlType(), et.getName());
				SQLDataType idef = getSQLType(dti);
				def = new SQLArrayTypeDefinition(idef);
			}
			break;
		default:
			break;
		}
		
		return def;
	}

	protected Integer getSize(DataType dataType) {
		Integer size = dataType.getSize();				
		return size;
	}

	private Integer getCharOctetLength(DataType dataType) {
		Integer size = dataType.getCharOctetLength();
		
		if (size == null) {
			int t = dataType.getDataType();
			
			switch (t) {
			case ValueType.CHAR:				
			case ValueType.VARCHAR:
				size = Integer.valueOf(1024 * 1024);
				break;
			case ValueType.LONGVARCHAR:				
			case ValueType.LONGNVARCHAR:
			default:
				break;
			}
		}
		
		return size;
	}

	protected abstract SchemaElementName newName(String typeName);
}
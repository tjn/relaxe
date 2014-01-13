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
import com.appspot.relaxe.expr.ddl.types.BooleanTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.CharTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.DateTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.DecimalTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.DoubleTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.FloatTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.IntTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.LongVarcharTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.NumericTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLArrayTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.SmallIntTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.TimeTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.TimestampTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.TinyIntTypeDefinition;
import com.appspot.relaxe.expr.ddl.types.TypeDefinition;
import com.appspot.relaxe.expr.ddl.types.VarcharTypeDefinition;
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
	public SQLTypeDefinition getSQLTypeDefinition(DataType dataType) {
		int t = dataType.getDataType();
		Integer s = getSize(dataType);
		
		SQLTypeDefinition def = null;
						
		switch (t) {
		case ValueType.INTEGER:
			def = IntTypeDefinition.get(null);
			break;
		case ValueType.BIGINT:
			def = BigIntTypeDefinition.get(null);	
			break;							
		case ValueType.SMALLINT:
			def = SmallIntTypeDefinition.get(null);
			break;										
		case ValueType.TINYINT:
			def = TinyIntTypeDefinition.get(null);
			break;
		case ValueType.VARCHAR:			
			def = VarcharTypeDefinition.get(getCharOctetLength(dataType));
			break;
		case ValueType.LONGVARCHAR:
			def = LongVarcharTypeDefinition.get(getCharOctetLength(dataType));
			break;
		case ValueType.CHAR:			
			def = CharTypeDefinition.get(getCharOctetLength(dataType));
			break;
		case ValueType.NUMERIC:			
			def = NumericTypeDefinition.get(s, Integer.valueOf(dataType.getDecimalDigits()));
			break;
		case ValueType.DECIMAL:			
			def = DecimalTypeDefinition.get(s, Integer.valueOf(dataType.getDecimalDigits()));
			break;
		case ValueType.TIMESTAMP:			
			def = TimestampTypeDefinition.get();
			break;
		case ValueType.DATE:			
			def = DateTypeDefinition.get();
			break;
		case ValueType.TIME:			
			def = TimeTypeDefinition.get();
			break;
		case ValueType.BOOLEAN:
			// fall-through
		case ValueType.BIT:			
			def = BooleanTypeDefinition.get();
			break;
		case ValueType.DOUBLE:			
			def = DoubleTypeDefinition.DEFINITION;
			break;
		case ValueType.FLOAT:			
			def = FloatTypeDefinition.DEFINITION;
			break;			
		case ValueType.DISTINCT:			
		case ValueType.OTHER:
			SchemaElementName name = newName(dataType.getTypeName());
			def = new TypeDefinition(name);
			break;
		case ValueType.ARRAY:			
			ValueType<?> pt = getType(dataType);
			
			if (pt != null) {
				com.appspot.relaxe.types.ArrayType<?, ?> at = pt.asArrayType();			
				ValueType<?> et = at.getElementType();			
				DataTypeImpl dti = new DataTypeImpl(et.getSqlType(), et.getName());
				SQLTypeDefinition idef = getSQLTypeDefinition(dti);
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
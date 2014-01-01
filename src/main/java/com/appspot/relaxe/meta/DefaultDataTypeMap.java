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
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.TimeType;
import com.appspot.relaxe.types.TimestampType;
import com.appspot.relaxe.types.VarcharType;

public abstract class DefaultDataTypeMap 
	implements DataTypeMap {

	public DefaultDataTypeMap() {
	}
	
	@Override
	public PrimitiveType<?> getType(DataType type) {
		switch (type.getDataType()) {
			case PrimitiveType.SMALLINT:
			case PrimitiveType.TINYINT:
			case PrimitiveType.INTEGER:
				return IntegerType.TYPE;
			case PrimitiveType.BIGINT:
				return LongType.TYPE;	
			case PrimitiveType.BOOLEAN:
				return BooleanType.TYPE;
			case PrimitiveType.VARCHAR:
				return VarcharType.TYPE;
			case PrimitiveType.LONGVARCHAR:
				return LongVarcharType.TYPE;
			case PrimitiveType.CHAR:			
				return CharType.TYPE;
			case PrimitiveType.NUMERIC:			
				return NumericType.TYPE;			
			case PrimitiveType.DECIMAL:			
				return DecimalType.TYPE;
			case PrimitiveType.TIMESTAMP:			
				return TimestampType.TYPE;
			case PrimitiveType.DATE:			
				return DateType.TYPE;		
			case PrimitiveType.TIME:			
				return TimeType.TYPE;
				
			// Implementation for the following three 
			case PrimitiveType.DISTINCT:			
			case PrimitiveType.OTHER:					
			case PrimitiveType.ARRAY:					
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
		case PrimitiveType.INTEGER:
			def = IntTypeDefinition.get(null);
			break;
		case PrimitiveType.BIGINT:
			def = BigIntTypeDefinition.get(null);	
			break;							
		case PrimitiveType.SMALLINT:
			def = SmallIntTypeDefinition.get(null);
			break;										
		case PrimitiveType.TINYINT:
			def = TinyIntTypeDefinition.get(null);
			break;
		case PrimitiveType.VARCHAR:			
			def = VarcharTypeDefinition.get(getCharOctetLength(dataType));
			break;
		case PrimitiveType.LONGVARCHAR:
			def = LongVarcharTypeDefinition.get(getCharOctetLength(dataType));
			break;
		case PrimitiveType.CHAR:			
			def = CharTypeDefinition.get(getCharOctetLength(dataType));
			break;
		case PrimitiveType.NUMERIC:			
			def = NumericTypeDefinition.get(s, Integer.valueOf(dataType.getDecimalDigits()));
			break;
		case PrimitiveType.DECIMAL:			
			def = DecimalTypeDefinition.get(s, Integer.valueOf(dataType.getDecimalDigits()));
			break;
		case PrimitiveType.TIMESTAMP:			
			def = TimestampTypeDefinition.get();
			break;
		case PrimitiveType.DATE:			
			def = DateTypeDefinition.get();
			break;
		case PrimitiveType.TIME:			
			def = TimeTypeDefinition.get();
			break;
		case PrimitiveType.BOOLEAN:
			// fall-through
		case PrimitiveType.BIT:			
			def = BooleanTypeDefinition.get();
			break;
		case PrimitiveType.DOUBLE:			
			def = DoubleTypeDefinition.DEFINITION;
			break;
		case PrimitiveType.FLOAT:			
			def = FloatTypeDefinition.DEFINITION;
			break;			
		case PrimitiveType.DISTINCT:			
		case PrimitiveType.OTHER:
			SchemaElementName name = newName(dataType.getTypeName());
			def = new TypeDefinition(name);
			break;
		case PrimitiveType.ARRAY:			
			PrimitiveType<?> pt = getType(dataType);
			
			if (pt != null) {
				com.appspot.relaxe.types.ArrayType<?, ?> at = pt.asArrayType();			
				PrimitiveType<?> et = at.getElementType();			
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
			case PrimitiveType.CHAR:				
			case PrimitiveType.VARCHAR:
				size = Integer.valueOf(1024 * 1024);
				break;
			case PrimitiveType.LONGVARCHAR:				
			case PrimitiveType.LONGNVARCHAR:
			default:
				break;
			}
		}
		
		return size;
	}

	protected abstract SchemaElementName newName(String typeName);
}
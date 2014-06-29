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
package com.appspot.relaxe;

import java.sql.Types;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.appspot.relaxe.ent.im.IntegerIdentityMap;
import com.appspot.relaxe.ent.im.LongIdentityMap;
import com.appspot.relaxe.ent.im.VarcharIdentityMap;
import com.appspot.relaxe.ent.value.BooleanAccessor;
import com.appspot.relaxe.ent.value.BooleanAttribute;
import com.appspot.relaxe.ent.value.CharAccessor;
import com.appspot.relaxe.ent.value.CharAttribute;
import com.appspot.relaxe.ent.value.DateAccessor;
import com.appspot.relaxe.ent.value.DateAttribute;
import com.appspot.relaxe.ent.value.DecimalAccessor;
import com.appspot.relaxe.ent.value.DecimalAttribute;
import com.appspot.relaxe.ent.value.DoubleAccessor;
import com.appspot.relaxe.ent.value.DoubleAttribute;
import com.appspot.relaxe.ent.value.HasBoolean;
import com.appspot.relaxe.ent.value.HasBooleanAttribute;
import com.appspot.relaxe.ent.value.HasChar;
import com.appspot.relaxe.ent.value.HasCharAttribute;
import com.appspot.relaxe.ent.value.HasDate;
import com.appspot.relaxe.ent.value.HasDateAttribute;
import com.appspot.relaxe.ent.value.HasDecimal;
import com.appspot.relaxe.ent.value.HasDecimalAttribute;
import com.appspot.relaxe.ent.value.HasDouble;
import com.appspot.relaxe.ent.value.HasDoubleAttribute;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerAttribute;
import com.appspot.relaxe.ent.value.HasLong;
import com.appspot.relaxe.ent.value.HasLongAttribute;
import com.appspot.relaxe.ent.value.HasLongVarBinary;
import com.appspot.relaxe.ent.value.HasLongVarBinaryAttribute;
import com.appspot.relaxe.ent.value.HasTime;
import com.appspot.relaxe.ent.value.HasTimeAttribute;
import com.appspot.relaxe.ent.value.HasTimestamp;
import com.appspot.relaxe.ent.value.HasTimestampAttribute;
import com.appspot.relaxe.ent.value.HasVarchar;
import com.appspot.relaxe.ent.value.HasVarcharAttribute;
import com.appspot.relaxe.ent.value.IntegerAccessor;
import com.appspot.relaxe.ent.value.IntegerAttribute;
import com.appspot.relaxe.ent.value.LongAccessor;
import com.appspot.relaxe.ent.value.LongAttribute;
import com.appspot.relaxe.ent.value.LongVarBinary;
import com.appspot.relaxe.ent.value.LongVarBinaryAccessor;
import com.appspot.relaxe.ent.value.LongVarBinaryAttribute;
import com.appspot.relaxe.ent.value.TimeAccessor;
import com.appspot.relaxe.ent.value.TimeAttribute;
import com.appspot.relaxe.ent.value.TimestampAccessor;
import com.appspot.relaxe.ent.value.TimestampAttribute;
import com.appspot.relaxe.ent.value.VarcharAccessor;
import com.appspot.relaxe.ent.value.VarcharAttribute;
import com.appspot.relaxe.map.AttributeDescriptor;
import com.appspot.relaxe.map.AttributeTypeMap;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.source.DefaultAttributeDescriptor;
import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.BooleanType;
import com.appspot.relaxe.types.CharType;
import com.appspot.relaxe.types.DateType;
import com.appspot.relaxe.types.DecimalType;
import com.appspot.relaxe.types.DistinctType;
import com.appspot.relaxe.types.DoubleType;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.LongVarBinaryType;
import com.appspot.relaxe.types.OtherType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.TimeType;
import com.appspot.relaxe.types.TimestampType;
import com.appspot.relaxe.types.VarcharType;
import com.appspot.relaxe.value.BooleanHolder;
import com.appspot.relaxe.value.CharHolder;
import com.appspot.relaxe.value.DateHolder;
import com.appspot.relaxe.value.Decimal;
import com.appspot.relaxe.value.DecimalHolder;
import com.appspot.relaxe.value.DoubleHolder;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.LongHolder;
import com.appspot.relaxe.value.LongVarBinaryHolder;
import com.appspot.relaxe.value.TimeHolder;
import com.appspot.relaxe.value.TimestampHolder;
import com.appspot.relaxe.value.VarcharHolder;



public class DefaultTypeMapper
	implements AttributeTypeMap {
	
	
	public static class Key
		implements Comparable<Key> {
		private int type;
		private String name;
		private int hash;
		
		public Key(int type, String name) {
			super();
			this.type = type;
			this.name = name;
			this.hash = (type ^ ((name == null) ? 0 : name.hashCode()));
		}
		
		
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || obj == this) {
				return (obj == this);
			}
			
			if (!(obj instanceof Key)) {
				return false;				
			}
			
			Key key = (Key) obj;
		
			return (key.type == this.type) && 
				   (this.name == null) ? (key.name == null) : this.name.equals(key.name);			
		}
		
		@Override
		public int hashCode() {
			return hash;
		}

		@Override
		public int compareTo(Key o) {
			int result = this.type - o.type;
			
			if (result != 0) {
				return result > 0 ? 1 : -1;
			}
			
			if (this.name == null) {
				return (o.name == null) ? 0 : 1;
			}
						
			return this.name.compareTo(o.name);
		}
	}
	
	
	
	
	
	private Map<String, AttributeDescriptor> otherAttributeTypeMap;
	private Map<String, AttributeDescriptor> distinctAttributeTypeMap;
	private Map<String, AttributeDescriptor> arrayAttributeTypeMap;
	
	private Map<Key, AttributeDescriptor> attributeTypeMap;
	
	protected void register(ValueType<?> type, AttributeDescriptor info) {
		register(type.getSqlType(), type.getName(), info);		
	}
	
	protected void register(int type, String name, AttributeDescriptor info) {
		getAttributeTypeMap().put(new Key(type, name), info);
	}
		
	protected void register(OtherType<?> otherType, AttributeDescriptor info) {		
		String name = otherType.getName();
						
//		if (!otherType.equals(info.getPrimitiveType())) {
//			throw new IllegalArgumentException("precondition 'otherType.equals(info.getPrimitiveType())' failed:\n" +
//					"otherType: " + otherType + "\n" +
//					"info type: " + info.getPrimitiveType() + "\n"
//			);
//		}
		
		getOtherAttributeTypeMap().put(name, info);				
	}
	
	protected void register(DistinctType<?> distinctType, AttributeDescriptor info) {		
		String name = distinctType.getName();
		getDistinctAttributeTypeMap().put(name, info);				
	}

	protected void register(ArrayType<?, ?> arrayType, AttributeDescriptor info) {		
		String name = arrayType.getName();
		getArrayAttributeTypeMap().put(name, info);				
	}
	

	protected AttributeDescriptor getOtherAttributeInfo(DataType dataType) {
		return getOtherAttributeTypeMap().get(dataType.getTypeName());
	}
	
	protected AttributeDescriptor getDistinctAttributeInfo(DataType dataType) {
		return getDistinctAttributeTypeMap().get(dataType.getTypeName());
	}
	
	protected AttributeDescriptor getArrayAttributeInfo(DataType dataType) {
		return getArrayAttributeTypeMap().get(dataType.getTypeName());
	}
	    
    @Override
	public AttributeDescriptor getAttributeDescriptor(DataType dataType) {
    	            	
        int type = dataType.getDataType();
                
        Map<Key, AttributeDescriptor> am = getAttributeTypeMap();
        
                
        AttributeDescriptor ai = am.isEmpty() ? null : am.get(new Key(dataType.getDataType(), dataType.getTypeName()));
        	
        if (ai != null) {        	
        	return ai;
        }
        
        DefaultAttributeDescriptor da = new DefaultAttributeDescriptor();
        
        
        switch (type) {
	        case Types.CHAR:
	        	da.setValueType(String.class);
	        	da.setHolderType(CharHolder.class);
	        	da.setAttributeType(CharAttribute.class);
	        	da.setAccessorType(CharAccessor.class);
	        	da.setPrimitiveType(CharType.TYPE);
	        	da.setReadableContainerType(HasChar.Read.class);
	        	da.setWritableContainerType(HasChar.Write.class);	        	
	        	da.setContainerMetaType(HasCharAttribute.class);
	        	break;
	        case Types.VARCHAR:
	        case Types.LONGVARCHAR:
	        	da.setValueType(String.class);
	        	da.setHolderType(VarcharHolder.class);
	        	da.setAttributeType(VarcharAttribute.class);
	        	da.setAccessorType(VarcharAccessor.class);
	        	da.setPrimitiveType(VarcharType.TYPE);        	
	        	da.setIdentityMapType(VarcharIdentityMap.class);	        	
	        	da.setReadableContainerType(HasVarchar.Read.class);
	        	da.setWritableContainerType(HasVarchar.Write.class);
	        	da.setContainerMetaType(HasVarcharAttribute.class);        	
	            break;            	
	        case Types.LONGNVARCHAR:
	        	break;        	
	        case Types.SMALLINT:        	
	        case Types.INTEGER:
	        case Types.TINYINT:
	        	da.setValueType(Integer.class);
	        	da.setHolderType(IntegerHolder.class);
	        	da.setAttributeType(IntegerAttribute.class);
	        	da.setAccessorType(IntegerAccessor.class);
	        	da.setPrimitiveType(IntegerType.TYPE);
	        	da.setIdentityMapType(IntegerIdentityMap.class);	        	
	        	da.setReadableContainerType(HasInteger.Read.class);
	        	da.setWritableContainerType(HasInteger.Write.class);
	        	da.setContainerMetaType(HasIntegerAttribute.class);
	            break;
	        case Types.BIGINT:
	        	da.setValueType(Long.class);
	        	da.setHolderType(LongHolder.class);
	        	da.setAttributeType(LongAttribute.class);
	        	da.setAccessorType(LongAccessor.class);
	        	da.setPrimitiveType(LongType.TYPE);
	        	da.setIdentityMapType(LongIdentityMap.class);
	        	da.setReadableContainerType(HasLong.Read.class);
	        	da.setWritableContainerType(HasLong.Write.class);
	        	da.setContainerMetaType(HasLongAttribute.class);	        	
	        	break;
	        case Types.BIT:
	        case Types.BOOLEAN:	        	
	        	Integer size = dataType.getSize();
	        	
	        	if (size != null && size.intValue() > 1) {
	        		break;
	        	}
	        	
	        	da.setValueType(Boolean.class);
	        	da.setHolderType(BooleanHolder.class);
	        	da.setAttributeType(BooleanAttribute.class);
	        	da.setAccessorType(BooleanAccessor.class);
	        	da.setPrimitiveType(BooleanType.TYPE);	        	
	        	da.setReadableContainerType(HasBoolean.Read.class);
	        	da.setWritableContainerType(HasBoolean.Write.class);
	        	da.setContainerMetaType(HasBooleanAttribute.class);	 
	        	break;
	        case Types.REAL:
	            break;
	        case Types.FLOAT:                
	        	// The recommended Java mapping for the FLOAT type is as a Java double, fall-through
	        case Types.DOUBLE:
	        	da.setValueType(Double.class);
	        	da.setHolderType(DoubleHolder.class);
	        	da.setAttributeType(DoubleAttribute.class);
	        	da.setAccessorType(DoubleAccessor.class);
	        	da.setPrimitiveType(DoubleType.TYPE);	        	
	        	da.setReadableContainerType(HasDouble.Read.class);
	        	da.setWritableContainerType(HasDouble.Write.class);	        	
	        	da.setContainerMetaType(HasDoubleAttribute.class);       	
	            break;
	        case Types.DECIMAL:
	        case Types.NUMERIC:
	        	da.setValueType(Decimal.class);
	        	da.setHolderType(DecimalHolder.class);
	        	da.setAttributeType(DecimalAttribute.class);
	        	da.setAccessorType(DecimalAccessor.class);
	        	da.setPrimitiveType(DecimalType.TYPE);	        	
	        	da.setReadableContainerType(HasDecimal.Read.class);
	        	da.setWritableContainerType(HasDecimal.Write.class);
	        	da.setContainerMetaType(HasDecimalAttribute.class);
	            break;        	
	        case Types.DATE:            
	        	da.setValueType(Date.class);
	        	da.setHolderType(DateHolder.class);
	        	da.setAttributeType(DateAttribute.class);
	        	da.setAccessorType(DateAccessor.class);
	        	da.setPrimitiveType(DateType.TYPE);	        	
	        	da.setReadableContainerType(HasDate.Read.class);
	        	da.setWritableContainerType(HasDate.Write.class);
	        	da.setContainerMetaType(HasDateAttribute.class);
	            break;
	            
	        case Types.TIME:            
	        	da.setValueType(Date.class);
	        	da.setHolderType(TimeHolder.class);
	        	da.setAttributeType(TimeAttribute.class);
	        	da.setAccessorType(TimeAccessor.class);
	        	da.setPrimitiveType(TimeType.TYPE);	        	
	        	da.setReadableContainerType(HasTime.Read.class);
	        	da.setWritableContainerType(HasTime.Write.class);
	        	da.setContainerMetaType(HasTimeAttribute.class);        	
	            break;
	            
	        case Types.BINARY:
	        case Types.VARBINARY:
	        case Types.LONGVARBINARY:
	        	da.setValueType(LongVarBinary.class);
	        	da.setHolderType(LongVarBinaryHolder.class);
	        	da.setAttributeType(LongVarBinaryAttribute.class);
	        	da.setAccessorType(LongVarBinaryAccessor.class);
	        	da.setPrimitiveType(LongVarBinaryType.TYPE);	        	
	        	da.setReadableContainerType(HasLongVarBinary.Read.class);
	        	da.setWritableContainerType(HasLongVarBinary.Write.class);	        	
	        	da.setContainerMetaType(HasLongVarBinaryAttribute.class);
	            break;
	            
	        case Types.TIMESTAMP:
	        	da.setValueType(Date.class);
	        	da.setHolderType(TimestampHolder.class);
	        	da.setAttributeType(TimestampAttribute.class);
	        	da.setAccessorType(TimestampAccessor.class);
	        	da.setPrimitiveType(TimestampType.TYPE);	        	
	        	da.setReadableContainerType(HasTimestamp.Read.class);
	        	da.setWritableContainerType(HasTimestamp.Write.class);
	        	da.setContainerMetaType(HasTimestampAttribute.class);
	            break;	            
	            
	        case Types.DISTINCT:
		        {	        		
	        		return getDistinctAttributeInfo(dataType);
		        }
	        case Types.OTHER:
	        	{		        
		        	return getOtherAttributeInfo(dataType);
	        	}	        
	        case Types.ARRAY:
	        	{		        	
		        	return getArrayAttributeInfo(dataType);	        		
	        	}
	        default:      
	            break;
	    }

    	
    	return da;
    }
    
    
    private Map<String, AttributeDescriptor> getOtherAttributeTypeMap() {
		if (otherAttributeTypeMap == null) {
			otherAttributeTypeMap = new TreeMap<String, AttributeDescriptor>();			
		}

		return otherAttributeTypeMap;
	}
    
    private Map<String, AttributeDescriptor> getDistinctAttributeTypeMap() {
		if (distinctAttributeTypeMap == null) {
			distinctAttributeTypeMap = new TreeMap<String, AttributeDescriptor>();
			
		}

		return distinctAttributeTypeMap;
	}
    
    public Map<String, AttributeDescriptor> getArrayAttributeTypeMap() {
		if (arrayAttributeTypeMap == null) {
			arrayAttributeTypeMap = new TreeMap<String, AttributeDescriptor>();
		}

		return arrayAttributeTypeMap;		
	}
    
    private Map<Key, AttributeDescriptor> getAttributeTypeMap() {
		if (attributeTypeMap == null) {
			attributeTypeMap = new TreeMap<Key, AttributeDescriptor>();			
		}

		return attributeTypeMap;
	}
}

    

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
import com.appspot.relaxe.ent.value.BooleanKey;
import com.appspot.relaxe.ent.value.CharAccessor;
import com.appspot.relaxe.ent.value.CharKey;
import com.appspot.relaxe.ent.value.DateAccessor;
import com.appspot.relaxe.ent.value.DateKey;
import com.appspot.relaxe.ent.value.DecimalAccessor;
import com.appspot.relaxe.ent.value.DecimalKey;
import com.appspot.relaxe.ent.value.DoubleAccessor;
import com.appspot.relaxe.ent.value.DoubleKey;
import com.appspot.relaxe.ent.value.HasBoolean;
import com.appspot.relaxe.ent.value.HasBooleanKey;
import com.appspot.relaxe.ent.value.HasChar;
import com.appspot.relaxe.ent.value.HasCharKey;
import com.appspot.relaxe.ent.value.HasDate;
import com.appspot.relaxe.ent.value.HasDateKey;
import com.appspot.relaxe.ent.value.HasDecimal;
import com.appspot.relaxe.ent.value.HasDecimalKey;
import com.appspot.relaxe.ent.value.HasDouble;
import com.appspot.relaxe.ent.value.HasDoubleKey;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerKey;
import com.appspot.relaxe.ent.value.HasLong;
import com.appspot.relaxe.ent.value.HasLongKey;
import com.appspot.relaxe.ent.value.HasLongVarBinary;
import com.appspot.relaxe.ent.value.HasLongVarBinaryKey;
import com.appspot.relaxe.ent.value.HasTime;
import com.appspot.relaxe.ent.value.HasTimeKey;
import com.appspot.relaxe.ent.value.HasTimestamp;
import com.appspot.relaxe.ent.value.HasTimestampKey;
import com.appspot.relaxe.ent.value.HasVarchar;
import com.appspot.relaxe.ent.value.HasVarcharKey;
import com.appspot.relaxe.ent.value.IntegerAccessor;
import com.appspot.relaxe.ent.value.IntegerKey;
import com.appspot.relaxe.ent.value.LongAccessor;
import com.appspot.relaxe.ent.value.LongKey;
import com.appspot.relaxe.ent.value.LongVarBinary;
import com.appspot.relaxe.ent.value.LongVarBinaryAccessor;
import com.appspot.relaxe.ent.value.LongVarBinaryKey;
import com.appspot.relaxe.ent.value.TimeAccessor;
import com.appspot.relaxe.ent.value.TimeKey;
import com.appspot.relaxe.ent.value.TimestampAccessor;
import com.appspot.relaxe.ent.value.TimestampKey;
import com.appspot.relaxe.ent.value.VarcharAccessor;
import com.appspot.relaxe.ent.value.VarcharKey;
import com.appspot.relaxe.map.AttributeInfo;
import com.appspot.relaxe.map.TypeMapper;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.rpc.BooleanHolder;
import com.appspot.relaxe.rpc.CharHolder;
import com.appspot.relaxe.rpc.DateHolder;
import com.appspot.relaxe.rpc.Decimal;
import com.appspot.relaxe.rpc.DecimalHolder;
import com.appspot.relaxe.rpc.DoubleHolder;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.rpc.LongVarBinaryHolder;
import com.appspot.relaxe.rpc.TimeHolder;
import com.appspot.relaxe.rpc.TimestampHolder;
import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.source.DefaultAttributeInfo;
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
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.TimeType;
import com.appspot.relaxe.types.TimestampType;
import com.appspot.relaxe.types.VarcharType;



public class DefaultTypeMapper
	implements TypeMapper {
	
	
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
	
	
	
	
	
	private Map<String, AttributeInfo> otherAttributeTypeMap;
	private Map<String, AttributeInfo> distinctAttributeTypeMap;
	private Map<String, AttributeInfo> arrayAttributeTypeMap;
	
	private Map<Key, AttributeInfo> attributeTypeMap;
	
	protected void register(PrimitiveType<?> type, AttributeInfo info) {
		register(type.getSqlType(), type.getName(), info);		
	}
	
	protected void register(int type, String name, AttributeInfo info) {
		getAttributeTypeMap().put(new Key(type, name), info);
	}
		
	protected void register(OtherType<?> otherType, AttributeInfo info) {		
		String name = otherType.getName();
						
//		if (!otherType.equals(info.getPrimitiveType())) {
//			throw new IllegalArgumentException("precondition 'otherType.equals(info.getPrimitiveType())' failed:\n" +
//					"otherType: " + otherType + "\n" +
//					"info type: " + info.getPrimitiveType() + "\n"
//			);
//		}
		
		getOtherAttributeTypeMap().put(name, info);				
	}
	
	protected void register(DistinctType<?> distinctType, AttributeInfo info) {		
		String name = distinctType.getName();
		getDistinctAttributeTypeMap().put(name, info);				
	}

	protected void register(ArrayType<?, ?> arrayType, AttributeInfo info) {		
		String name = arrayType.getName();
		getArrayAttributeTypeMap().put(name, info);				
	}
	

	protected AttributeInfo getOtherAttributeInfo(DataType dataType) {
		return getOtherAttributeTypeMap().get(dataType.getTypeName());
	}
	
	protected AttributeInfo getDistinctAttributeInfo(DataType dataType) {
		return getDistinctAttributeTypeMap().get(dataType.getTypeName());
	}
	
	protected AttributeInfo getArrayAttributeInfo(DataType dataType) {
		return getArrayAttributeTypeMap().get(dataType.getTypeName());
	}
	    
    @Override
	public AttributeInfo getAttributeInfo(DataType dataType) {
    	            	
        int type = dataType.getDataType();
                
        Map<Key, AttributeInfo> am = getAttributeTypeMap();
        
                
        AttributeInfo ai = am.isEmpty() ? null : am.get(new Key(dataType.getDataType(), dataType.getTypeName()));
        	
        if (ai != null) {        	
        	return ai;
        }
        
        DefaultAttributeInfo da = new DefaultAttributeInfo();
        
        
        switch (type) {
	        case Types.CHAR:
	        	da.setValueType(String.class);
	        	da.setHolderType(CharHolder.class);
	        	da.setAttributeType(CharKey.class);
	        	da.setAccessorType(CharAccessor.class);
	        	da.setPrimitiveType(CharType.TYPE);        	
	        	da.setContainerType(HasChar.class);
	        	da.setContainerMetaType(HasCharKey.class);
	        	break;
	        case Types.VARCHAR:
	        case Types.LONGVARCHAR:
	        	da.setValueType(String.class);
	        	da.setHolderType(VarcharHolder.class);
	        	da.setAttributeType(VarcharKey.class);
	        	da.setAccessorType(VarcharAccessor.class);
	        	da.setPrimitiveType(VarcharType.TYPE);        	
	        	da.setIdentityMapType(VarcharIdentityMap.class);
	        	da.setContainerType(HasVarchar.class);
	        	da.setContainerMetaType(HasVarcharKey.class);        	
	            break;            	
	        case Types.LONGNVARCHAR:
	        	break;        	
	        case Types.SMALLINT:        	
	        case Types.INTEGER:
	        case Types.TINYINT:
	        	da.setValueType(Integer.class);
	        	da.setHolderType(IntegerHolder.class);
	        	da.setAttributeType(IntegerKey.class);
	        	da.setAccessorType(IntegerAccessor.class);
	        	da.setPrimitiveType(IntegerType.TYPE);
	        	da.setIdentityMapType(IntegerIdentityMap.class);
	        	da.setContainerType(HasInteger.class);
	        	da.setContainerMetaType(HasIntegerKey.class);
	            break;
	        case Types.BIGINT:
	        	da.setValueType(Long.class);
	        	da.setHolderType(LongHolder.class);
	        	da.setAttributeType(LongKey.class);
	        	da.setAccessorType(LongAccessor.class);
	        	da.setPrimitiveType(LongType.TYPE);
	        	da.setIdentityMapType(LongIdentityMap.class);
	        	da.setContainerType(HasLong.class);
	        	da.setContainerMetaType(HasLongKey.class);	        	
	        	break;
	        case Types.BIT:
	        case Types.BOOLEAN:	        	
	        	Integer size = dataType.getSize();
	        	
	        	if (size != null && size.intValue() > 1) {
	        		break;
	        	}
	        	
	        	da.setValueType(Boolean.class);
	        	da.setHolderType(BooleanHolder.class);
	        	da.setAttributeType(BooleanKey.class);
	        	da.setAccessorType(BooleanAccessor.class);
	        	da.setPrimitiveType(BooleanType.TYPE);	        	
	        	da.setContainerType(HasBoolean.class);
	        	da.setContainerMetaType(HasBooleanKey.class);	 
	        	break;
	        case Types.REAL:
	            break;
	        case Types.FLOAT:                
	        	// The recommended Java mapping for the FLOAT type is as a Java double, fall-through
	        case Types.DOUBLE:
	        	da.setValueType(Double.class);
	        	da.setHolderType(DoubleHolder.class);
	        	da.setAttributeType(DoubleKey.class);
	        	da.setAccessorType(DoubleAccessor.class);
	        	da.setPrimitiveType(DoubleType.TYPE);
	        	da.setContainerType(HasDouble.class);
	        	da.setContainerMetaType(HasDoubleKey.class);       	
	            break;
	        case Types.DECIMAL:
	        case Types.NUMERIC:
	        	da.setValueType(Decimal.class);
	        	da.setHolderType(DecimalHolder.class);
	        	da.setAttributeType(DecimalKey.class);
	        	da.setAccessorType(DecimalAccessor.class);
	        	da.setPrimitiveType(DecimalType.TYPE);
	        	da.setContainerType(HasDecimal.class);
	        	da.setContainerMetaType(HasDecimalKey.class);
	            break;        	
	        case Types.DATE:            
	        	da.setValueType(Date.class);
	        	da.setHolderType(DateHolder.class);
	        	da.setAttributeType(DateKey.class);
	        	da.setAccessorType(DateAccessor.class);
	        	da.setPrimitiveType(DateType.TYPE);
	        	da.setContainerType(HasDate.class);
	        	da.setContainerMetaType(HasDateKey.class);
	            break;
	            
	        case Types.TIME:            
	        	da.setValueType(Date.class);
	        	da.setHolderType(TimeHolder.class);
	        	da.setAttributeType(TimeKey.class);
	        	da.setAccessorType(TimeAccessor.class);
	        	da.setPrimitiveType(TimeType.TYPE);
	        	da.setContainerType(HasTime.class);
	        	da.setContainerMetaType(HasTimeKey.class);        	
	            break;
	            
	        case Types.BINARY:
	        case Types.VARBINARY:
	        case Types.LONGVARBINARY:
	        	da.setValueType(LongVarBinary.class);
	        	da.setHolderType(LongVarBinaryHolder.class);
	        	da.setAttributeType(LongVarBinaryKey.class);
	        	da.setAccessorType(LongVarBinaryAccessor.class);
	        	da.setPrimitiveType(LongVarBinaryType.TYPE);
	        	da.setContainerType(HasLongVarBinary.class);
	        	da.setContainerMetaType(HasLongVarBinaryKey.class);
	            break;
	            
	        case Types.TIMESTAMP:
	        	da.setValueType(Date.class);
	        	da.setHolderType(TimestampHolder.class);
	        	da.setAttributeType(TimestampKey.class);
	        	da.setAccessorType(TimestampAccessor.class);
	        	da.setPrimitiveType(TimestampType.TYPE);
	        	da.setContainerType(HasTimestamp.class);
	        	da.setContainerMetaType(HasTimestampKey.class);
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
    
    
    private Map<String, AttributeInfo> getOtherAttributeTypeMap() {
		if (otherAttributeTypeMap == null) {
			otherAttributeTypeMap = new TreeMap<String, AttributeInfo>();			
		}

		return otherAttributeTypeMap;
	}
    
    private Map<String, AttributeInfo> getDistinctAttributeTypeMap() {
		if (distinctAttributeTypeMap == null) {
			distinctAttributeTypeMap = new TreeMap<String, AttributeInfo>();
			
		}

		return distinctAttributeTypeMap;
	}
    
    public Map<String, AttributeInfo> getArrayAttributeTypeMap() {
		if (arrayAttributeTypeMap == null) {
			arrayAttributeTypeMap = new TreeMap<String, AttributeInfo>();
		}

		return arrayAttributeTypeMap;		
	}
    
    private Map<Key, AttributeInfo> getAttributeTypeMap() {
		if (attributeTypeMap == null) {
			attributeTypeMap = new TreeMap<Key, AttributeInfo>();			
		}

		return attributeTypeMap;
	}
}

    

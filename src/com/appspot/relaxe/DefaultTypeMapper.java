/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.Table;
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
import com.appspot.relaxe.types.TimeType;
import com.appspot.relaxe.types.TimestampType;
import com.appspot.relaxe.types.VarcharType;



public class DefaultTypeMapper
	implements TypeMapper {
	
	private Map<String, AttributeInfo> otherAttributeTypeMap;
	private Map<String, AttributeInfo> distinctAttributeTypeMap;
	private Map<String, AttributeInfo> arrayAttributeTypeMap;
	
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
	

	protected AttributeInfo getOtherAttributeInfo(Table table, Column c, String columnTypeName) {
		return getOtherAttributeTypeMap().get(columnTypeName);
	}
	
	protected AttributeInfo getDistinctAttributeInfo(Table table, Column c, String columnTypeName) {
		return getDistinctAttributeTypeMap().get(columnTypeName);
	}
	
	protected AttributeInfo getArrayAttributeInfo(Table table, Column c, String columnTypeName) {
		return getArrayAttributeTypeMap().get(columnTypeName);
	}
	    
    @Override
	public AttributeInfo getAttributeInfo(Table table, Column c) {
    	DefaultAttributeInfo da = new DefaultAttributeInfo();
        
    	DataType dataType = c.getDataType();
        int type = dataType.getDataType();
        
        switch (type) {
	        case Types.CHAR:
	        	da.setAttributeType(String.class);
	        	da.setHolderType(CharHolder.class);
	        	da.setKeyType(CharKey.class);
	        	da.setAccessorType(CharAccessor.class);
	        	da.setPrimitiveType(CharType.TYPE);        	
	        	da.setContainerType(HasChar.class);
	        	da.setContainerMetaType(HasCharKey.class);
	        	break;
	        case Types.VARCHAR:
	        case Types.LONGVARCHAR:
	        	da.setAttributeType(String.class);
	        	da.setHolderType(VarcharHolder.class);
	        	da.setKeyType(VarcharKey.class);
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
	        	da.setAttributeType(Integer.class);
	        	da.setHolderType(IntegerHolder.class);
	        	da.setKeyType(IntegerKey.class);
	        	da.setAccessorType(IntegerAccessor.class);
	        	da.setPrimitiveType(IntegerType.TYPE);
	        	da.setIdentityMapType(IntegerIdentityMap.class);
	        	da.setContainerType(HasInteger.class);
	        	da.setContainerMetaType(HasIntegerKey.class);
	            break;
	        case Types.BIGINT:
	        	da.setAttributeType(Long.class);
	        	da.setHolderType(LongHolder.class);
	        	da.setKeyType(LongKey.class);
	        	da.setAccessorType(LongAccessor.class);
	        	da.setPrimitiveType(LongType.TYPE);
	        	da.setIdentityMapType(LongIdentityMap.class);
	        	da.setContainerType(HasLong.class);
	        	da.setContainerMetaType(HasLongKey.class);	        	
	        	break;
	        case Types.BIT:
	        case Types.BOOLEAN:
	        	if (dataType.getSize() > 1) {
	        		break;
	        	}
	        	
	        	da.setAttributeType(Boolean.class);
	        	da.setHolderType(BooleanHolder.class);
	        	da.setKeyType(BooleanKey.class);
	        	da.setAccessorType(BooleanAccessor.class);
	        	da.setPrimitiveType(BooleanType.TYPE);	        	
	        	da.setContainerType(HasBoolean.class);
	        	da.setContainerMetaType(HasBooleanKey.class);	 
	        	break;
	        case Types.REAL:
	            break;
	        case Types.FLOAT:                
	        case Types.DOUBLE:
	        	da.setAttributeType(Double.class);
	        	da.setHolderType(DoubleHolder.class);
	        	da.setKeyType(DoubleKey.class);
	        	da.setAccessorType(DoubleAccessor.class);
	        	da.setPrimitiveType(DoubleType.TYPE);
	        	da.setContainerType(HasDouble.class);
	        	da.setContainerMetaType(HasDoubleKey.class);       	
	            break;
	        case Types.DECIMAL:
	        case Types.NUMERIC:
	        	da.setAttributeType(Decimal.class);
	        	da.setHolderType(DecimalHolder.class);
	        	da.setKeyType(DecimalKey.class);
	        	da.setAccessorType(DecimalAccessor.class);
	        	da.setPrimitiveType(DecimalType.TYPE);
	        	da.setContainerType(HasDecimal.class);
	        	da.setContainerMetaType(HasDecimalKey.class);
	            break;        	
	        case Types.DATE:            
	        	da.setAttributeType(Date.class);
	        	da.setHolderType(DateHolder.class);
	        	da.setKeyType(DateKey.class);
	        	da.setAccessorType(DateAccessor.class);
	        	da.setPrimitiveType(DateType.TYPE);
	        	da.setContainerType(HasDate.class);
	        	da.setContainerMetaType(HasDateKey.class);
	            break;
	            
	        case Types.TIME:            
	        	da.setAttributeType(Date.class);
	        	da.setHolderType(TimeHolder.class);
	        	da.setKeyType(TimeKey.class);
	        	da.setAccessorType(TimeAccessor.class);
	        	da.setPrimitiveType(TimeType.TYPE);
	        	da.setContainerType(HasTime.class);
	        	da.setContainerMetaType(HasTimeKey.class);        	
	            break;
	            
	        case Types.BINARY:
	        case Types.VARBINARY:
	        case Types.LONGVARBINARY:
	        	da.setAttributeType(LongVarBinary.class);
	        	da.setHolderType(LongVarBinaryHolder.class);
	        	da.setKeyType(LongVarBinaryKey.class);
	        	da.setAccessorType(LongVarBinaryAccessor.class);
	        	da.setPrimitiveType(LongVarBinaryType.TYPE);
	        	da.setContainerType(HasLongVarBinary.class);
	        	da.setContainerMetaType(HasLongVarBinaryKey.class);
	            break;
	            
	        case Types.TIMESTAMP:
	        	da.setAttributeType(Date.class);
	        	da.setHolderType(TimestampHolder.class);
	        	da.setKeyType(TimestampKey.class);
	        	da.setAccessorType(TimestampAccessor.class);
	        	da.setPrimitiveType(TimestampType.TYPE);
	        	da.setContainerType(HasTimestamp.class);
	        	da.setContainerMetaType(HasTimestampKey.class);
	            break;	            
	            
	        case Types.DISTINCT:
		        {
	        		String typeName = c.getDataType().getTypeName();
	        		return getDistinctAttributeInfo(table, c, typeName);
		        }
	        case Types.OTHER:
	        	{
		        	String typeName = c.getDataType().getTypeName();
		        	return getOtherAttributeInfo(table, c, typeName);
	        	}	        
	        case Types.ARRAY:
	        	{
		        	String typeName = c.getDataType().getTypeName();
		        	return getArrayAttributeInfo(table, c, typeName);	        		
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
}

    

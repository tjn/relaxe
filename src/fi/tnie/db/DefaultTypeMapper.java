/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Types;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


import fi.tnie.db.ent.im.IntegerIdentityMap;
import fi.tnie.db.ent.im.VarcharIdentityMap;
import fi.tnie.db.ent.value.BooleanAccessor;
import fi.tnie.db.ent.value.BooleanKey;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.CharAccessor;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DateAccessor;
import fi.tnie.db.ent.value.DecimalAccessor;
import fi.tnie.db.ent.value.DecimalKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.DoubleAccessor;
import fi.tnie.db.ent.value.HasBoolean;
import fi.tnie.db.ent.value.HasBooleanKey;
import fi.tnie.db.ent.value.HasChar;
import fi.tnie.db.ent.value.HasCharKey;
import fi.tnie.db.ent.value.HasDate;
import fi.tnie.db.ent.value.HasDateKey;
import fi.tnie.db.ent.value.HasDecimal;
import fi.tnie.db.ent.value.HasDecimalKey;
import fi.tnie.db.ent.value.HasDouble;
import fi.tnie.db.ent.value.HasDoubleKey;
import fi.tnie.db.ent.value.HasInteger;
import fi.tnie.db.ent.value.HasIntegerKey;
import fi.tnie.db.ent.value.HasLongVarBinary;
import fi.tnie.db.ent.value.HasLongVarBinaryKey;
import fi.tnie.db.ent.value.HasTime;
import fi.tnie.db.ent.value.HasTimeKey;
import fi.tnie.db.ent.value.HasTimestamp;
import fi.tnie.db.ent.value.HasTimestampKey;
import fi.tnie.db.ent.value.HasVarchar;
import fi.tnie.db.ent.value.HasVarcharKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntegerAccessor;
import fi.tnie.db.ent.value.LongVarBinary;
import fi.tnie.db.ent.value.LongVarBinaryAccessor;
import fi.tnie.db.ent.value.LongVarBinaryKey;
import fi.tnie.db.ent.value.TimeAccessor;
import fi.tnie.db.ent.value.TimeKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.TimestampAccessor;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.ent.value.VarcharAccessor;
import fi.tnie.db.map.AttributeInfo;
import fi.tnie.db.map.TypeMapper;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.Table;
import fi.tnie.db.rpc.BooleanHolder;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.Decimal;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.LongVarBinaryHolder;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.source.DefaultAttributeInfo;
import fi.tnie.db.types.ArrayType;
import fi.tnie.db.types.BooleanType;
import fi.tnie.db.types.CharType;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.DecimalType;
import fi.tnie.db.types.DistinctType;
import fi.tnie.db.types.DoubleType;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.LongVarBinaryType;
import fi.tnie.db.types.OtherType;
import fi.tnie.db.types.TimeType;
import fi.tnie.db.types.TimestampType;
import fi.tnie.db.types.VarcharType;

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
	        	break;
	        case Types.BIT:
	        	if (dataType.getSize() > 1) {
	        		break;
	        	}	        	
	        case Types.BOOLEAN:
	        	da.setAttributeType(Boolean.class);
	        	da.setHolderType(BooleanHolder.class);
	        	da.setKeyType(BooleanKey.class);
	        	da.setAccessorType(BooleanAccessor.class);
	        	da.setPrimitiveType(BooleanType.TYPE);	        	
	        	da.setContainerType(HasBoolean.class);
	        	da.setContainerMetaType(HasBooleanKey.class);	                       
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

    

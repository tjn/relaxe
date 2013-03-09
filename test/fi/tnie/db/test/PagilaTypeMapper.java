/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import org.apache.log4j.Logger;

import fi.tnie.db.DefaultTypeMapper;
import fi.tnie.db.ent.im.IntegerIdentityMap;
import fi.tnie.db.ent.value.HasInteger;
import fi.tnie.db.ent.value.HasIntegerKey;
import fi.tnie.db.ent.value.HasInterval;
import fi.tnie.db.ent.value.HasIntervalKey;
import fi.tnie.db.ent.value.IntegerAccessor;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalAccessor;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.env.pg.PGTextArrayAccessor;
import fi.tnie.db.env.pg.PGTextArrayHolder;
import fi.tnie.db.env.pg.PGTextArrayKey;
import fi.tnie.db.env.pg.PGTextArrayType;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.rpc.StringArray;
import fi.tnie.db.source.DefaultAttributeInfo;
import fi.tnie.db.types.ArrayType;
import fi.tnie.db.types.DistinctType;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.IntervalType;
import fi.tnie.db.types.OtherType;

public class PagilaTypeMapper
	extends DefaultTypeMapper {
	
	private static Logger logger = Logger.getLogger(PagilaTypeMapper.class);
	

	public PagilaTypeMapper() {
		super();
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
						
			info.setPrimitiveType(MPAARatingType.TYPE);
			info.setAttributeType(MPAARating.class);		
			info.setHolderType(MPAARatingHolder.class);
			info.setKeyType(MPAARatingKey.class);
			info.setContainerType(HasMPAARating.class);
			info.setContainerMetaType(HasMPAARatingKey.class);
			
			info.setIdentityMapType(null);		
			info.setAccessorType(MPAARatingAccessor.class);
			
			registerOtherType(MPAARatingType.TYPE, info);
		}
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
			info.setPrimitiveType(IntegerType.TYPE);
			info.setAttributeType(Integer.class);		
			info.setHolderType(IntegerHolder.class);
			info.setKeyType(IntegerKey.class);
			info.setContainerType(HasInteger.class);
			info.setContainerMetaType(HasIntegerKey.class);			
			info.setIdentityMapType(IntegerIdentityMap.class);		
			info.setAccessorType(IntegerAccessor.class);
						
			registerDistinctType(YearType.TYPE, info);
		}
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setAttributeType(Interval.YearMonth.class);
        	info.setHolderType(IntervalHolder.YearMonth.class);
        	info.setKeyType(IntervalKey.YearMonth.class);
        	info.setAccessorType(IntervalAccessor.YearMonth.class);
        	info.setPrimitiveType(IntervalType.YearMonth.TYPE);
        	info.setContainerType(HasInterval.YearMonth.class);
        	info.setContainerMetaType(HasIntervalKey.YearMonth.class);
						
			registerDistinctType(YearMonthIntervalType.TYPE, info);						
		}
		
		
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setAttributeType(StringArray.class);
        	info.setHolderType(PGTextArrayHolder.class);
        	info.setKeyType(PGTextArrayKey.class);
        	info.setAccessorType(PGTextArrayAccessor.class);
        	info.setPrimitiveType(PGTextArrayType.TYPE);
        	info.setContainerType(HasPGTextArray.class);
        	info.setContainerMetaType(HasPGTextArrayKey.class);
						
			registerArrayType(PGTextArrayType.TYPE, info);						
		}
		


//		{
//			DefaultAttributeInfo info = new DefaultAttributeInfo();
//			
//			info.setAttributeType(String.class);		
//			info.setHolderType(VarcharHolder.class);
//			info.setKeyType(VarcharKey.class);
//			info.setContainerType(HasVarchar.class);
//			info.setContainerMetaType(HasVarcharKey.class);
//			
//			info.setIdentityMapType(null);		
//			info.setAccessorType(null);
//			
//			registerOtherType(FullTextType.TYPE, info);
//		}		
		
//		{
//			DefaultAttributeInfo info = new DefaultAttributeInfo();
//			
//			info.setAttributeType(MPAARating.class);		
//			info.setHolderType(MPAARatingHolder.class);
//			info.setKeyType(MPAARatingKey.class);
//			info.setContainerType(HasMPAARating.class);
//			info.setContainerMetaType(HasMPAARatingKey.class);
//			
//			info.setIdentityMapType(null);		
//			info.setAccessorType(null);
//				
//			registerOtherType(MPAARatingType.TYPE, info);
//		}
	}
	
	
	private void registerOtherType(OtherType<?> t, DefaultAttributeInfo info) {
//		info.setPrimitiveType(t);
		logger().debug("primitive-type 1: " + t);
		logger().debug("primitive-type 2: " + info.getPrimitiveType());
		register(t, info);
	}
	
	private void registerDistinctType(DistinctType<?> t, DefaultAttributeInfo info) {
//		info.setPrimitiveType(t);
		logger().debug("primitive-type 1: " + t);
		logger().debug("primitive-type 2: " + info.getPrimitiveType());
		register(t, info);
	}
	
	private void registerArrayType(ArrayType<?, ?> t, DefaultAttributeInfo info) {
//		info.setPrimitiveType(t);
		logger().debug("primitive-type 1: " + t);
		logger().debug("primitive-type 2: " + info.getPrimitiveType());
		register(t, info);
	}

	

	private static Logger logger() {
		return PagilaTypeMapper.logger;
	}
	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import org.apache.log4j.Logger;

import com.appspot.relaxe.DefaultTypeMapper;
import com.appspot.relaxe.ent.im.IntegerIdentityMap;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerKey;
import com.appspot.relaxe.ent.value.HasInterval;
import com.appspot.relaxe.ent.value.HasIntervalKey;
import com.appspot.relaxe.ent.value.HasVarchar;
import com.appspot.relaxe.ent.value.HasVarcharKey;
import com.appspot.relaxe.ent.value.IntegerAccessor;
import com.appspot.relaxe.ent.value.IntegerKey;
import com.appspot.relaxe.ent.value.IntervalAccessor;
import com.appspot.relaxe.ent.value.IntervalKey;
import com.appspot.relaxe.ent.value.VarcharAccessor;
import com.appspot.relaxe.ent.value.VarcharKey;
import com.appspot.relaxe.env.pg.PGTSVectorType;
import com.appspot.relaxe.env.pg.PGTextArrayAccessor;
import com.appspot.relaxe.env.pg.PGTextArrayHolder;
import com.appspot.relaxe.env.pg.PGTextArrayKey;
import com.appspot.relaxe.env.pg.PGTextArrayType;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.Interval;
import com.appspot.relaxe.rpc.IntervalHolder;
import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.source.DefaultAttributeInfo;
import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.DistinctType;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.IntervalType;
import com.appspot.relaxe.types.OtherType;
import com.appspot.relaxe.types.VarcharType;


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
		
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setAttributeType(String.class);
        	info.setHolderType(VarcharHolder.class);
        	info.setKeyType(VarcharKey.class);
        	info.setAccessorType(VarcharAccessor.class);
        	info.setPrimitiveType(VarcharType.TYPE);
        	info.setContainerType(HasVarchar.class);
        	info.setContainerMetaType(HasVarcharKey.class);
						
			registerOtherType(PGTSVectorType.TYPE, info);						
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

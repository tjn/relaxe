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
package com.appspot.relaxe.pg.pagila;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.appspot.relaxe.env.pg.HasPGText;
import com.appspot.relaxe.env.pg.HasPGTextArray;
import com.appspot.relaxe.env.pg.HasPGTextArrayKey;
import com.appspot.relaxe.env.pg.HasPGTextKey;
import com.appspot.relaxe.env.pg.PGTSVectorType;
import com.appspot.relaxe.env.pg.PGTextAccessor;
import com.appspot.relaxe.env.pg.PGTextArrayAccessor;
import com.appspot.relaxe.env.pg.PGTextArrayHolder;
import com.appspot.relaxe.env.pg.PGTextArrayKey;
import com.appspot.relaxe.env.pg.PGTextArrayType;
import com.appspot.relaxe.env.pg.PGTextHolder;
import com.appspot.relaxe.env.pg.PGTextKey;
import com.appspot.relaxe.env.pg.PGTextType;
import com.appspot.relaxe.pg.pagila.types.HasMPAARating;
import com.appspot.relaxe.pg.pagila.types.HasMPAARatingKey;
import com.appspot.relaxe.pg.pagila.types.MPAARating;
import com.appspot.relaxe.pg.pagila.types.MPAARatingAccessor;
import com.appspot.relaxe.pg.pagila.types.MPAARatingHolder;
import com.appspot.relaxe.pg.pagila.types.MPAARatingKey;
import com.appspot.relaxe.pg.pagila.types.MPAARatingType;
import com.appspot.relaxe.pg.pagila.types.YearMonthIntervalType;
import com.appspot.relaxe.pg.pagila.types.YearType;
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
	
	private static Logger logger = LoggerFactory.getLogger(PagilaTypeMapper.class);

	public PagilaTypeMapper() {
		super();
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setValueType(String.class);
        	info.setHolderType(PGTextHolder.class);
        	info.setKeyType(PGTextKey.class);
        	info.setAccessorType(PGTextAccessor.class);
        	info.setPrimitiveType(PGTextType.TYPE);
        	info.setContainerType(HasPGText.class);
        	info.setContainerMetaType(HasPGTextKey.class);
        	        	        	
        	register(PGTextType.TYPE.getSqlType(), PGTextType.TYPE.getName(), info);
		}
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setValueType(String.class);
        	info.setHolderType(VarcharHolder.class);
        	info.setKeyType(VarcharKey.class);
        	info.setAccessorType(VarcharAccessor.class);
        	info.setPrimitiveType(VarcharType.TYPE);
        	info.setContainerType(HasVarchar.class);
        	info.setContainerMetaType(HasVarcharKey.class);
        	        	        	
        	register(PGTSVectorType.TYPE, info);
		}		
		
//		{
//			DefaultAttributeInfo info = new DefaultAttributeInfo();
//			
//        	info.setAttributeType(String.class);
//        	info.setHolderType(PGTSVectorType.class);
//        	info.setKeyType(PGTSVectorKey.class);
//        	info.setAccessorType(PGTextAccessor.class);
//        	info.setPrimitiveType(PGTextType.TYPE);
//        	info.setContainerType(HasPGText.class);
//        	info.setContainerMetaType(HasPGTextKey.class);
//        	        	        	
//        	register(PGTextType.TYPE.getSqlType(), PGTextType.TYPE.getName(), info);
//		}				
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
						
			info.setPrimitiveType(MPAARatingType.TYPE);
			info.setValueType(MPAARating.class);		
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
			info.setValueType(Integer.class);		
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
			
        	info.setValueType(Interval.YearMonth.class);
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
			
        	info.setValueType(StringArray.class);
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
			
        	info.setValueType(String.class);
        	info.setHolderType(PGTextHolder.class);
        	info.setKeyType(PGTextKey.class);
        	info.setAccessorType(IntervalAccessor.YearMonth.class);
        	info.setPrimitiveType(IntervalType.YearMonth.TYPE);
        	info.setContainerType(HasInterval.YearMonth.class);
        	info.setContainerMetaType(HasIntervalKey.YearMonth.class);
						
			registerDistinctType(YearMonthIntervalType.TYPE, info);						
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

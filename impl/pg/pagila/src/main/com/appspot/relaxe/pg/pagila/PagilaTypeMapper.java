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
import com.appspot.relaxe.common.pagila.types.HasMPAARating;
import com.appspot.relaxe.common.pagila.types.HasMPAARatingAttribute;
import com.appspot.relaxe.common.pagila.types.MPAARating;
import com.appspot.relaxe.common.pagila.types.MPAARatingAccessor;
import com.appspot.relaxe.common.pagila.types.MPAARatingHolder;
import com.appspot.relaxe.common.pagila.types.MPAARatingKey;
import com.appspot.relaxe.common.pagila.types.MPAARatingType;
import com.appspot.relaxe.common.pagila.types.YearMonthIntervalType;
import com.appspot.relaxe.common.pagila.types.YearType;
import com.appspot.relaxe.ent.im.IntegerIdentityMap;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerAttribute;
import com.appspot.relaxe.ent.value.HasInterval;
import com.appspot.relaxe.ent.value.HasIntervalAttribute;
import com.appspot.relaxe.ent.value.HasVarchar;
import com.appspot.relaxe.ent.value.HasVarcharAttribute;
import com.appspot.relaxe.ent.value.IntegerAccessor;
import com.appspot.relaxe.ent.value.IntegerAttribute;
import com.appspot.relaxe.ent.value.IntervalAccessor;
import com.appspot.relaxe.ent.value.IntervalAttribute;
import com.appspot.relaxe.ent.value.VarcharAccessor;
import com.appspot.relaxe.ent.value.VarcharAttribute;
import com.appspot.relaxe.env.pg.HasPGText;
import com.appspot.relaxe.env.pg.HasPGTextArray;
import com.appspot.relaxe.env.pg.HasPGTextArrayAttribute;
import com.appspot.relaxe.env.pg.HasPGTextAttribute;
import com.appspot.relaxe.env.pg.PGTSVectorType;
import com.appspot.relaxe.env.pg.PGTextAccessor;
import com.appspot.relaxe.env.pg.PGTextArrayAccessor;
import com.appspot.relaxe.env.pg.PGTextArrayAttribute;
import com.appspot.relaxe.env.pg.PGTextArrayHolder;
import com.appspot.relaxe.env.pg.PGTextArrayType;
import com.appspot.relaxe.env.pg.PGTextAttribute;
import com.appspot.relaxe.env.pg.PGTextHolder;
import com.appspot.relaxe.env.pg.PGTextType;
import com.appspot.relaxe.source.DefaultAttributeInfo;
import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.DistinctType;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.IntervalType;
import com.appspot.relaxe.types.OtherType;
import com.appspot.relaxe.types.VarcharType;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.Interval;
import com.appspot.relaxe.value.IntervalHolder;
import com.appspot.relaxe.value.StringArray;
import com.appspot.relaxe.value.VarcharHolder;


public class PagilaTypeMapper
	extends DefaultTypeMapper {
	
	private static Logger logger = LoggerFactory.getLogger(PagilaTypeMapper.class);

	public PagilaTypeMapper() {
		super();
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setValueType(String.class);
        	info.setHolderType(PGTextHolder.class);
        	info.setAttributeType(PGTextAttribute.class);
        	info.setAccessorType(PGTextAccessor.class);
        	info.setPrimitiveType(PGTextType.TYPE);
        	info.setContainerType(HasPGText.class);
        	info.setContainerMetaType(HasPGTextAttribute.class);
        	        	        	
        	register(PGTextType.TYPE.getSqlType(), PGTextType.TYPE.getName(), info);
		}
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setValueType(String.class);
        	info.setHolderType(VarcharHolder.class);
        	info.setAttributeType(VarcharAttribute.class);
        	info.setAccessorType(VarcharAccessor.class);
        	info.setPrimitiveType(VarcharType.TYPE);
        	info.setContainerType(HasVarchar.class);
        	info.setContainerMetaType(HasVarcharAttribute.class);
        	        	        	
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
			info.setAttributeType(MPAARatingKey.class);
			info.setContainerType(HasMPAARating.class);
			info.setContainerMetaType(HasMPAARatingAttribute.class);
			
			info.setIdentityMapType(null);		
			info.setAccessorType(MPAARatingAccessor.class);
			
			registerOtherType(MPAARatingType.TYPE, info);
		}
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
			info.setPrimitiveType(IntegerType.TYPE);
			info.setValueType(Integer.class);		
			info.setHolderType(IntegerHolder.class);
			info.setAttributeType(IntegerAttribute.class);
			info.setContainerType(HasInteger.class);
			info.setContainerMetaType(HasIntegerAttribute.class);			
			info.setIdentityMapType(IntegerIdentityMap.class);		
			info.setAccessorType(IntegerAccessor.class);
						
			registerDistinctType(YearType.TYPE, info);
		}
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setValueType(Interval.YearMonth.class);
        	info.setHolderType(IntervalHolder.YearMonth.class);
        	info.setAttributeType(IntervalAttribute.YearMonth.class);
        	info.setAccessorType(IntervalAccessor.YearMonth.class);
        	info.setPrimitiveType(IntervalType.YearMonth.TYPE);
        	info.setContainerType(HasInterval.YearMonth.class);
        	info.setContainerMetaType(HasIntervalAttribute.YearMonth.class);
						
			registerDistinctType(YearMonthIntervalType.TYPE, info);						
		}
		
		
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setValueType(StringArray.class);
        	info.setHolderType(PGTextArrayHolder.class);
        	info.setAttributeType(PGTextArrayAttribute.class);
        	info.setAccessorType(PGTextArrayAccessor.class);
        	info.setPrimitiveType(PGTextArrayType.TYPE);
        	info.setContainerType(HasPGTextArray.class);
        	info.setContainerMetaType(HasPGTextArrayAttribute.class);
						
			registerArrayType(PGTextArrayType.TYPE, info);						
		}
		
		{
			DefaultAttributeInfo info = new DefaultAttributeInfo();
			
        	info.setValueType(String.class);
        	info.setHolderType(PGTextHolder.class);
        	info.setAttributeType(PGTextAttribute.class);
        	info.setAccessorType(IntervalAccessor.YearMonth.class);
        	info.setPrimitiveType(IntervalType.YearMonth.TYPE);
        	info.setContainerType(HasInterval.YearMonth.class);
        	info.setContainerMetaType(HasIntervalAttribute.YearMonth.class);
						
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

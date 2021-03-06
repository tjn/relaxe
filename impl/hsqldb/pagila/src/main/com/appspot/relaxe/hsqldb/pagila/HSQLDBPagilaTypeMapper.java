package com.appspot.relaxe.hsqldb.pagila;
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

import com.appspot.relaxe.DefaultTypeMapper;
import com.appspot.relaxe.ent.value.HasVarcharArray;
import com.appspot.relaxe.ent.value.HasVarcharArrayAttribute;
import com.appspot.relaxe.ent.value.VarcharArrayAccessor;
import com.appspot.relaxe.ent.value.VarcharArrayAttribute;
import com.appspot.relaxe.source.DefaultAttributeDescriptor;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.VarcharArrayType;
import com.appspot.relaxe.value.StringArray;
import com.appspot.relaxe.value.VarcharArrayHolder;

public class HSQLDBPagilaTypeMapper
	extends DefaultTypeMapper {
	
	public HSQLDBPagilaTypeMapper() {
		super();
		
		DefaultAttributeDescriptor info = new DefaultAttributeDescriptor();
    	info.setValueType(StringArray.class);
    	info.setHolderType(VarcharArrayHolder.class);
    	info.setAttributeType(VarcharArrayAttribute.class);
    	info.setAccessorType(VarcharArrayAccessor.class);
    	info.setPrimitiveType(VarcharArrayType.TYPE);
    	info.setReadableContainerType(HasVarcharArray.Read.class);
    	info.setWritableContainerType(HasVarcharArray.Write.class);
    	info.setContainerMetaType(HasVarcharArrayAttribute.class);
		
		register(ValueType.ARRAY, "VARCHAR(1024) ARRAY", info);
	}	
}

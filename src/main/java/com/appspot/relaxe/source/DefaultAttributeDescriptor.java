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
package com.appspot.relaxe.source;

import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.map.AttributeDescriptor;
import com.appspot.relaxe.types.ValueType;


public class DefaultAttributeDescriptor
	implements AttributeDescriptor {
	
	private Class<?> valueType;
	private Class<?> holderType;
	private Class<?> attributeType;
	private Class<?> accessorType;
	private Class<?> identityMapType;
	private Class<?> readableContainerType;
	private Class<?> writableContainerType;
	private Class<?> containerMetaType;
	
	private ValueType<?> primitiveType;
	
	private static Map<Class<?>, Class<?>> primitiveTypeMap = new HashMap<Class<?>, Class<?>>();
	
	static {
		primitiveTypeMap.put(Boolean.class, Boolean.TYPE);
		primitiveTypeMap.put(Byte.class, Byte.TYPE);
		primitiveTypeMap.put(Double.class, Double.TYPE);
		primitiveTypeMap.put(Float.class, Float.TYPE);
		primitiveTypeMap.put(Integer.class, Integer.TYPE);
		primitiveTypeMap.put(Long.class, Long.TYPE);
		primitiveTypeMap.put(Short.class, Short.TYPE);		
	}
	
	@Override
	public Class<?> getValueType() {
		return this.valueType;
	}

	public DefaultAttributeDescriptor() {
		super();
	}

	@Override
	public Class<?> getHolderType() {
		return this.holderType;
	}

	@Override
	public Class<?> getAttributeType() {
		return this.attributeType;
	}

	@Override
	public Class<?> getAccessorType() {
		return this.accessorType;
	}

	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	public void setHolderType(Class<?> holderType) {
		this.holderType = holderType;
	}

	public void setAttributeType(Class<?> attributeType) {
		this.attributeType = attributeType;
	}

	public void setAccessorType(Class<?> valueType) {
		this.accessorType = valueType;
	}

	@Override
	public ValueType<?> getPrimitiveType() {
		return primitiveType;
	}

	public void setPrimitiveType(ValueType<?> primitiveType) {
		this.primitiveType = primitiveType;
	}

	@Override
	public Class<?> getIdentityMapType() {
		return identityMapType;
	}

	public void setIdentityMapType(Class<?> identityMapType) {
		this.identityMapType = identityMapType;
	}

	@Override
	public Class<?> getReadableContainerType() {
		return readableContainerType;
	}
	
	@Override
	public Class<?> getWritableContainerType() {
		return writableContainerType;
	}	

	public void setReadableContainerType(Class<?> containerType) {
		this.readableContainerType = containerType;
	}
	
	public void setWritableContainerType(Class<?> containerType) {
		this.writableContainerType = containerType;
	}

	@Override
	public Class<?> getContainerMetaType() {
		return containerMetaType;
	}

	public void setContainerMetaType(Class<?> containerMetaType) {
		this.containerMetaType = containerMetaType;
	}
	
	
	
}

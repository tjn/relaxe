/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.map;

import com.appspot.relaxe.types.AbstractPrimitiveType;

public interface AttributeInfo {
	
//	DataType getColumnType();
	Class<?> getHolderType();
	Class<?> getKeyType();
	Class<?> getAccessorType();
	Class<?> getAttributeType();
	Class<?> getIdentityMapType();	
	Class<?> getContainerType();
	Class<?> getContainerMetaType();
	
	AbstractPrimitiveType<?> getPrimitiveType();

	
}
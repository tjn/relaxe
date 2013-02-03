/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.map;

import fi.tnie.db.types.PrimitiveType;

public interface AttributeInfo {
	
//	DataType getColumnType();
	Class<?> getHolderType();
	Class<?> getKeyType();
	Class<?> getAccessorType();
	Class<?> getAttributeType();
	Class<?> getIdentityMapType();	
	Class<?> getContainerType();
	Class<?> getContainerMetaType();
	
	PrimitiveType<?> getPrimitiveType();

	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.map;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;
import fi.tnie.db.types.PrimitiveType;

public interface AttributeInfo {

	Table getTable();
	Column getColumn();
	Class<?> getHolderType();
	Class<?> getKeyType();
	Class<?> getAccessorType();
	Class<?> getAttributeType();
	Class<?> getIdentityMapType();
	PrimitiveType<?> getPrimitiveType();

	
}

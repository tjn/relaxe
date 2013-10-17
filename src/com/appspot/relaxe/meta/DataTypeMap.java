/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.types.PrimitiveType;

public interface DataTypeMap {

	PrimitiveType<?> getType(DataType type);		
	SQLTypeDefinition getSQLTypeDefinition(DataType dataType);
}

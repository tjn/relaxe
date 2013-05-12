/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaName;

public interface Schema
	extends MetaObject {

	Environment getEnvironment();
	Identifier getCatalogName();
	@Override
	Identifier getUnqualifiedName();
	SchemaName getName();
			
	SchemaElementMap<Table> tables();	
	SchemaElementMap<ForeignKey> foreignKeys();
	SchemaElementMap<PrimaryKey> primaryKeys();
	SchemaElementMap<BaseTable> baseTables();
	
	SchemaElementMap<Constraint> constraints();
	
}

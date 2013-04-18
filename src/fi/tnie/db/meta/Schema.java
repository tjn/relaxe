/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaName;

public interface Schema
	extends MetaObject {

	Environment getEnvironment();
	Identifier getCatalogName();
	Identifier getUnqualifiedName();
	SchemaName getName();
			
	SchemaElementMap<Table> tables();	
	SchemaElementMap<ForeignKey> foreignKeys();
	SchemaElementMap<PrimaryKey> primaryKeys();
	SchemaElementMap<BaseTable> baseTables();
	
	SchemaElementMap<Constraint> constraints();
	
}

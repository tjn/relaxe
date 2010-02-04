/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.Identifier;

public interface Schema
	extends MetaObject {

	public Catalog getCatalog();
	public Identifier getUnqualifiedName();	
		
	
	SchemaElementMap<? extends Table> tables();	
	SchemaElementMap<? extends ForeignKey> foreignKeys();
	SchemaElementMap<? extends PrimaryKey> primaryKeys();
	SchemaElementMap<? extends BaseTable> baseTables();
	
	SchemaElementMap<? extends Constraint> constraints();
	
}

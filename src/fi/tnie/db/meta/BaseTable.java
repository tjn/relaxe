/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

public interface BaseTable
	extends Table {

	SchemaElementMap<ForeignKey> references();	
	
	SchemaElementMap<ForeignKey> foreignKeys();	
	
	PrimaryKey getPrimaryKey();	
}

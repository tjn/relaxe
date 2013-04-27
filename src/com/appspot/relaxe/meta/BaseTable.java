/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

public interface BaseTable
	extends Table {

	SchemaElementMap<ForeignKey> foreignKeys();
	PrimaryKey getPrimaryKey();
	
	boolean isPrimaryKeyColumn(Column column);
}

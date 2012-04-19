/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.List;

import fi.tnie.db.expr.Identifier;

public interface Table
	extends SchemaElement {
	
	String BASE_TABLE = "TABLE";	
	String VIEW = "VIEW";
	String SYSTEM_TABLE = "SYSTEM TABLE";
	String getTableType();
	
	ColumnMap columnMap();
		
	Column getColumn(Identifier cn);	
	List<? extends Column> columns();
	boolean isBaseTable();
}

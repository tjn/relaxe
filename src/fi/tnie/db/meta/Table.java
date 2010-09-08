/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.List;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.impl.ColumnMap;

public interface Table
	extends SchemaElement {
	
	String BASE_TABLE = "TABLE";	
	String getTableType();
	
	ColumnMap columnMap();
		
	Column getColumn(Identifier cn);	
	List<? extends Column> columns();
	boolean isBaseTable();
}

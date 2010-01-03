/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Map;

public interface Table
	extends MetaElement {
	
	String BASE_TABLE = "TABLE";
	
	String getName();
	String getTableType();
	
	Map<String, Column> columns();

	boolean isBaseTable();
}

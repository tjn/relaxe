/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

public interface Table
	extends SchemaElement {
	
	String BASE_TABLE = "TABLE";	
	String VIEW = "VIEW";
	String SYSTEM_TABLE = "SYSTEM TABLE";
	
	String getTableType();
	ColumnMap columnMap();
	boolean isBaseTable();
	
	BaseTable asBaseTable();
}
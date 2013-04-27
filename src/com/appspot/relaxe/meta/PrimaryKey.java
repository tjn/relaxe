/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

public interface PrimaryKey 
	extends Constraint {

	BaseTable getTable();	
	ColumnMap getColumnMap();

}

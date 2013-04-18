/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

public interface PrimaryKey 
	extends Constraint {

	BaseTable getTable();	
	ColumnMap getColumnMap();

}

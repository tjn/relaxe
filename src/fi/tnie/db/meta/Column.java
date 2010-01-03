/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

public interface Column 
	extends MetaObject {

	DataType getDataType();	
	boolean isPrimaryKeyColumn();
	Boolean isAutoIncrement();
}

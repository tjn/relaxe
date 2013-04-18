/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.ColumnName;

public interface Column 
	extends MetaObject {
    
	DataType getDataType();
	
	/** 
	 * 
	 * @return
	 */
	boolean isDefinitelyNotNullable();
	
	/**
	 * 
	 * @return
	 */	
	Boolean isAutoIncrement();	
	ColumnName getColumnName(); 
}

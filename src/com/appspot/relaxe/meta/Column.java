/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.ColumnName;

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

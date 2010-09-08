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
	boolean isPrimaryKeyColumn();
	Boolean isAutoIncrement();	
	public ColumnName getColumnName(); 
}

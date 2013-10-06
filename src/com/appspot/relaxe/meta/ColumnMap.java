/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;


public interface ColumnMap
	extends ElementMap<Column> {
	
	/**
	 * Returns a column by 0-based index.
	 * 
	 * @param index
	 * @return
	 */
	Column get(int index);	
}

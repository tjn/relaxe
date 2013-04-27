/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;


public interface ColumnMap
	extends ElementMap<Column> {
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	Column get(int index);	
}

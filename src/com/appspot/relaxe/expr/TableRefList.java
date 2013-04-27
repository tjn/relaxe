/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public interface TableRefList
	extends Element	{
	
	public int getCount();
	public AbstractTableReference getItem(int i);
}

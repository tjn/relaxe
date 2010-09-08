/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public interface TableRefList
	extends Element	{
	
	public int getCount();
	public AbstractTableReference getItem(int i);
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public interface MutableRow
	extends Row {
	void set(int column, Object value);	
}

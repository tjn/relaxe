/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public interface DataObject
{	
	Holder get(Identifiable a);
	void set(Identifiable a, Holder h);
}

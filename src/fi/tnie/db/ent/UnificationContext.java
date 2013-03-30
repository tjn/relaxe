/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


public interface UnificationContext
{	
	public void close();
	public void add(ContextRegistration r);	
}

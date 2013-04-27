/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;


public interface UnificationContext
{	
	public void close();
	public void add(ContextRegistration r);	
}

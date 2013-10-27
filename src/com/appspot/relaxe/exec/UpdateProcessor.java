/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.exec;


public interface UpdateProcessor
	extends ResultProcessor {
	
	public void updated(int count);
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ui;

public interface Dialog<
	U extends UI<U>
> 
	extends Popup
{	
	void setTitle(String title);
	Pane<U> getContentPane();		
}

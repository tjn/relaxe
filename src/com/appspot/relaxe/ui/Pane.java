/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ui;

public interface Pane<
	U extends UI<U>
> {	
	Pane<U> getParentPane();
	U getUI();	
	void setView(View<U> content);
}

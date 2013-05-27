/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ui;

public interface UI<U extends UI<U>> {
	
	U self();	
	Pane<U> getRootPane();
	PopupPane<U> newPopupPane();
	Dialog<U> newDialog();	
	ViewPane<U> newViewPane();
}

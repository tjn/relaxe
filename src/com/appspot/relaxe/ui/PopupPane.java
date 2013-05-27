/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ui;

public interface PopupPane<U extends UI<U>>
	extends Popup {	
	Pane<U> getContentPane();	
}

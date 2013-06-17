/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

public interface PageReceiver<R> {
	void receive(R result);
		
}

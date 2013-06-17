/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;

import com.appspot.relaxe.ent.FetchOptions;


public interface Fetcher<
	Q,
	T extends Serializable, 
	R extends PageReceiver<T>
> {	
	void fetch(Q queryTemplate, FetchOptions opts, R resultReceiver, PageReceiver<Throwable> errorReceiver);
}

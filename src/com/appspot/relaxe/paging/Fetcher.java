/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;

import com.appspot.relaxe.ent.FetchOptions;


public interface Fetcher<
	Q,
	T extends Serializable, 
	R extends Receiver<T>
> {	
	void fetch(Q queryTemplate, FetchOptions opts, R resultReceiver, Receiver<Throwable> errorReceiver);
}

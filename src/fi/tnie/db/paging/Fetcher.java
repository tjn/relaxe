/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.ent.FetchOptions;

public interface Fetcher<
	Q,
	T extends Serializable, 
	R extends Receiver<T>
> {	
	void fetch(Q queryTemplate, FetchOptions opts, R resultReceiver, Receiver<Throwable> errorReceiver);
}

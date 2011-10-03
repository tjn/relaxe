/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

public interface Fetcher<
	Q, T extends Serializable, R extends Receiver<T>> {
	
	void fetch(Q queryTemplate, Long limit, Long offset, R receiver);
}

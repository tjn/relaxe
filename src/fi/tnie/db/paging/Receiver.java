/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

public interface Receiver<R> {
	void receive(R result);
		
}

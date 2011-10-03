/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

public interface PagingEventHandler<P extends Pager<P, X>, X> {
		 
	void handleEvent(PagingEvent<P, X> e);		
}

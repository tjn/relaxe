/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

public interface PagingEventHandler<T extends Serializable, P extends Pager<T, P, X>, X> {
		 
	void handleEvent(PagingEvent<T, P, X> e);		
}

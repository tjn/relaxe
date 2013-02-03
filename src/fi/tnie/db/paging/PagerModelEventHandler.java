/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

public interface PagerModelEventHandler<T extends Serializable, P extends PagerModel<T, P, X>, X> {
		 
	void handleEvent(PagerModelEvent<T, P, X> e);		
}

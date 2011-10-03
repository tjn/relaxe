/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import fi.tnie.db.model.Registration;

/**
 * @param <P> Type of the Pager 
 * @param <C> Type of the paging command applicable with this pager 
 */
public interface Pager<P extends Pager<P, C>, C> {
		
	public Registration addPagingEventListener(PagingEventHandler<P, C> listener);
	
	void run(C command);
	
	
}

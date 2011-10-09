/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.model.Registration;
import fi.tnie.db.ui.action.Action;

/**
 * @param <T> Type of the page this pager browses.
 * @param <P> Type of the pager itself 
 * @param <C> Type of the paging command applicable with this pager 
 */
public interface Pager<T extends Serializable, P extends Pager<T, P, C>, C> {
		
	Registration addPagingEventListener(PagingEventHandler<T, P, C> listener);		
	Action getAction(C command);
	
	T getCurrentPage();
}

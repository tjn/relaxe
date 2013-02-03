/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.model.Registration;
import fi.tnie.db.ui.action.Action;

/**
 * @param <T> Type of the page(s) this pager browses.
 * @param <P> Type of the pager itself 
 * @param <C> Type of the paging command applicable with this pager 
 */
public interface PagerModel<T extends Serializable, P extends PagerModel<T, P, C>, C> {
		
	Registration addPagingEventListener(PagerModelEventHandler<T, P, C> listener);		
	Action getAction(C command);
	
	T getCurrentPage();
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;

import com.appspot.relaxe.model.Registration;
import com.appspot.relaxe.ui.action.Action;


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

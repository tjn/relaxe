/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.model.BooleanModel;
import fi.tnie.db.model.Registration;

public abstract class AbstractPager<T extends Serializable, P extends Pager<T, P, C>, C> {

	private Map<Registration, PagingEventHandler<T, P, C>> handlerMap;
		
	private Map<Registration, PagingEventHandler<T, P, C>> getHandlerMap() {
		if (handlerMap == null) {
			handlerMap = new HashMap<Registration, PagingEventHandler<T, P, C>>();			
		}

		return handlerMap;
	}
		
	public Registration addPagingEventListener(PagingEventHandler<T, P, C> handler) {
		if (handler == null) {
			throw new NullPointerException("handler");
		}
		
		final Map<Registration, PagingEventHandler<T, P, C>> hm = getHandlerMap();
		
		Registration reg = new Registration() {			
			@Override
			public void remove() {
				hm.remove(this);
			}
		};
		
		hm.put(reg, handler);
		
		return reg;
	}	
	
	protected void fireEvent(PagingEvent<T, P, C> newEvent) {
		for (PagingEventHandler<T, P, C> h : getHandlerMap().values()) {
			h.handleEvent(newEvent);			
		}
	}

	public abstract BooleanModel hasPreviousPage();
	public abstract BooleanModel hasNextPage();

	public abstract P self();
	
	
}

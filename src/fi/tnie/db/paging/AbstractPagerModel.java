/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.model.BooleanModel;
import fi.tnie.db.model.Registration;

public abstract class AbstractPagerModel<T extends Serializable, P extends PagerModel<T, P, C>, C> {

	private Map<Registration, PagerModelEventHandler<T, P, C>> handlerMap;
		
	private Map<Registration, PagerModelEventHandler<T, P, C>> getHandlerMap() {
		if (handlerMap == null) {
			handlerMap = new HashMap<Registration, PagerModelEventHandler<T, P, C>>();			
		}

		return handlerMap;
	}
		
	public Registration addPagingEventListener(PagerModelEventHandler<T, P, C> handler) {
		if (handler == null) {
			throw new NullPointerException("handler");
		}
		
		final Map<Registration, PagerModelEventHandler<T, P, C>> hm = getHandlerMap();
		
		Registration reg = new Registration() {			
			@Override
			public void remove() {
				hm.remove(this);
			}
		};
		
		hm.put(reg, handler);
		
		return reg;
	}	
	
	protected void fireEvent(PagerModelEvent<T, P, C> newEvent) {
		for (PagerModelEventHandler<T, P, C> h : getHandlerMap().values()) {
			h.handleEvent(newEvent);			
		}
	}

	public abstract BooleanModel hasPreviousPage();
	public abstract BooleanModel hasNextPage();

	public abstract P self();
	
	
}

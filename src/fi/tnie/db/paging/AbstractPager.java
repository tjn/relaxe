/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.model.IntegerModel;
import fi.tnie.db.model.Registration;

public abstract class AbstractPager<P extends Pager<P, C>, C>
	implements Pager<P, C> {

	private Map<Registration, PagingEventHandler<P, C>> handlerMap;
	
	private Map<Registration, PagingEventHandler<P, C>> getHandlerMap() {
		if (handlerMap == null) {
			handlerMap = new HashMap<Registration, PagingEventHandler<P, C>>();			
		}

		return handlerMap;
	}
	
	@Override
	public Registration addPagingEventListener(PagingEventHandler<P, C> handler) {
		if (handler == null) {
			throw new NullPointerException("handler");
		}
		
		final Map<Registration, PagingEventHandler<P, C>> hm = getHandlerMap();
		
		Registration reg = new Registration() {			
			@Override
			public void remove() {
				hm.remove(this);
			}
		};
		
		hm.put(reg, handler);
		
		return reg;
	}	
	
	protected void fireEvent(PagingEvent<P, C> newEvent) {
		for (PagingEventHandler<P, C> h : getHandlerMap().values()) {
			h.handleEvent(newEvent);			
		}
	}
	
	public abstract IntegerModel getPageSize();
}

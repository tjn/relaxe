/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.model.Registration;

public interface Pager<
	Q,
	E extends Serializable,
	P extends Page<E>,	
	G extends Pager<Q, E, P, G>	
> {	
	enum State {
		IDLE,
		LOADING
	}
	
	boolean firstPage();
	boolean nextPage();
	boolean previousPage();		
	boolean refresh();
	boolean lastPage();
	boolean previous();
	boolean next();	
		
	boolean fetch(int page, int index);
	
	/** 
	 * In the current page, set the current element the selection to the selected index.
	 * 
	 * @param index
	 * @return
	 */
	boolean select(int index);
	
	/**
	 * The current element on the current page.
	 * 
	 * @return
	 */
	E get();
					
	P getCurrentPage();
	State getState();
	
	void setQuery(Q query);
	Q getQuery();
	
	Registration addPagerEventHandler(PagerEventHandler<G> handler);
	
	enum Flags {
		LOAD_STATE,		
		LOAD_FAILURE,
		INDEX,
		PAGE,
	}	
	
	/**
	 * Index of the current element in the element list of the current page.
	 * 
	 * Returns <code>null</code> if there's no current page or the current page is empty. 
	 *  
	 * @return
	 */
	Integer index();
	
	int getPageSize();
		
	boolean isFirstElement();
	boolean isLastElement();	
	
	public G self();
}

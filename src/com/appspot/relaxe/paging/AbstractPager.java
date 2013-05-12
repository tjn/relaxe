/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.model.Registration;


public abstract class AbstractPager<
	Q,	
	E extends Serializable,
	R extends Page<E>,	
	G extends Pager<Q, E, R, G>
>
	implements Pager<Q, E, R, G> {
	
	private Integer index = null;
	private R currentPage;
	private Pager.State state = State.IDLE; 
		
	private Map<Registration, PagerEventHandler<G>> handlerMap;
		
	private final PagerEvent<G> INDEX = new PagerEvent<G>(self(), Flags.INDEX);  
	private final PagerEvent<G> LOAD_STATE = new PagerEvent<G>(self(), Flags.LOAD_STATE);
	private final PagerEvent<G> LOAD_FAILURE = new PagerEvent<G>(self(), Flags.LOAD_STATE, Flags.LOAD_FAILURE);
	private final PagerEvent<G> ALL = new PagerEvent<G>(self(), Flags.INDEX, Flags.PAGE, Flags.LOAD_STATE);
		
	private int pageSize;
	
	private Q query;
		
	private Fetcher<Q, R, Receiver<R>> fetcher; 
		
	private final Receiver<Throwable> errorReceiver = new Receiver<Throwable>() {		
		@Override
		public void receive(Throwable result) {			
			lastError = result;			
			fireEvent(State.IDLE, LOAD_FAILURE);
		}
	};
		
	private Throwable lastError;
		
	private List<E> getContent() {
		return (currentPage == null) ? null : currentPage.getContent();
	}
	
	@Override
	public Integer index() {		 		
		return this.index;
	}
		
	@Override
	public E get() {
		Integer x = index(); 		
		return (x == null) ? null : getContent().get(x.intValue());
	}
	
//	@Override
//	public boolean hasPreviousPage() {
//		return (offset() > 0);
//	}
	
	public AbstractPager(Fetcher<Q, R, Receiver<R>> fetcher, int pageSize) {
		super();		
		setFetcher(fetcher);
		
		if (pageSize < 1) {
			throw new IllegalArgumentException("invalid pageSize: " + pageSize);
		}
		
		this.pageSize = pageSize;
	}
	
	public AbstractPager(Fetcher<Q, R, Receiver<R>> fetcher, int pageSize, Q query) {
		this(fetcher, pageSize);
		setQuery(query);
	}

	@Override
	public boolean isFirstElement() {
		Integer x = index();
		return x != null && x.intValue() == 0;
	}
		
	@Override
	public boolean isLastElement() {
		Integer x = index();
		return x != null && (getContent().size() == (x.intValue() + 1));
	}

	@Override
	public boolean next() {
		if (isLoading()) {
			return false;
		}

		Integer cx = index();
		
		if (cx == null) {
			return false;
		}
		
		int nx = cx.intValue() + 1;
		int size = getContent().size();
		
		if (nx < size) {
			setIndex(Integer.valueOf(nx));
			fireEvent(INDEX);
		}
		else {
			loadNext(0);
		}
		
		return true;
	}

	private void loadNext(int nextIndex) {
		R cp = this.getCurrentPage();
		int ps = getPageSize();
		long no = (cp == null) ? 0 : cp.offset() + ps;   
		
		load(no, ps, nextIndex);		
	}
		
	private void loadPrevious(int nextIndex) {
		R cp = this.getCurrentPage();
		int ps = getPageSize();
		long no = (cp == null) ? 0 : cp.offset() - ps;
		load(no, ps, nextIndex);		
	}

	private void load(long no, int ps, final int nextIndex) {		
		fireEvent(State.LOADING, LOAD_STATE);
		
		Receiver<R> r = new Receiver<R>() {
			@Override
			public void receive(R result) {
				received(result, nextIndex);				
			}
		};
		
		FetchOptions opts = new FetchOptions(ps, no);
		getFetcher().fetch(getQuery(), opts, r, errorReceiver);
	}
	
	private void fireEvent(Pager.State newState, PagerEvent<G> event) {
		this.state = newState;
		fireEvent(event);		
	}
	
	private void fireEvent(PagerEvent<G> event) {
		if (this.handlerMap == null || this.handlerMap.isEmpty()) {
			return;
		}
		
		for (PagerEventHandler<G> h : handlerMap.values()) {
			h.handleEvent(event);
		}		
	}
	

	@Override
	public boolean previous() {		
		if (isLoading()) {
			return false;
		}

		Integer cx = index();
		
		if (cx == null) {
			return false;
		}
		
		int nx = cx.intValue() - 1;
				
		if (nx >= 0) {
			setIndex(Integer.valueOf(nx));
			fireEvent(INDEX);
		}
		else {
			if (offset() == 0) {
				return false;
			}			
			
			loadPrevious(getPageSize() - 1);			
		}
		
		return true;
	}

	@Override
	public Registration addPagerEventHandler(final PagerEventHandler<G> handler) {				
		Registration r = new Registration() {			
			@Override
			public void remove() {
				getHandlerMap().remove(this);
			}
		};
		
		getHandlerMap().put(r, handler);
		
		return r;
	}

	@Override
	public boolean firstPage() {
		if (isLoading()) {
			return false;
		}
		
		load(0, getPageSize(), 0);		
		return true;
	}

	@Override
	public R getCurrentPage() {
		return this.currentPage;
	}

	@Override
	public int getPageSize() {
		return this.pageSize;
	}

	@Override
	public Pager.State getState() {
		return this.state;
	}
	
	@Override
	public boolean refresh() {
		if (isLoading()) {
			return false;
		}
		
		Integer cx = index();
		int nx = (cx == null) ? 0 : cx.intValue();		
		load(offset(), getPageSize(), nx);
						
		return true;
	}

	@Override
	public boolean lastPage() {
		return false;
	}

	@Override
	public boolean nextPage() {
		if (isLoading()) {
			return false;
		}
		
		Integer cx = index();
		int nx = (cx == null) ? 0 : cx.intValue();
		
		loadNext(nx);					
		return true;
	}

	@Override
	public boolean previousPage() {
		if (isLoading()) {
			return false;
		}
		
		if (offset() == 0) {
			return false;
		}			
			
		Integer cx = index();
		int nx = (cx == null) ? 0 : cx.intValue();
		
		loadPrevious(nx);						
		return true;
	}
	
	
	@Override
	public boolean fetch(int page, int index) {
		if (isLoading()) {
			return false;
		}
		
		long off = page * getPageSize();		
		load(off, getPageSize(), index);
		
		return true;		
	}
	
	@Override
	public boolean select(int index) {		
		if (index < 0) {
			return false;
		}
		
		List<E> cl = getContent();
		
		if (cl == null || cl.isEmpty() || index >= cl.size()) {
			return false;
		}
		
		if (index().intValue() == index) {
			return false;
		}
		
		setIndex(Integer.valueOf(index));
		fireEvent(INDEX);
		
		return true;
	}


	private Map<Registration, PagerEventHandler<G>> getHandlerMap() {
		if (handlerMap == null) {
			handlerMap = new TreeMap<Registration, PagerEventHandler<G>>();			
		}

		return handlerMap;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	private boolean isLoading() {
		return (this.state == State.LOADING);
	}
	
	public void setCurrentPage(R currentPage) {
		this.currentPage = currentPage;
	}
	
	private void setIndex(Integer index) {
		this.index = index;
	}
	
	private void received(R result, int nextIndex) {
		List<E> content = result.getContent();
		int cs = content.size();
		
		if (nextIndex >= cs) {
			nextIndex = cs - 1;
		}
				
		setIndex((nextIndex < 0) ? null : Integer.valueOf(nextIndex));		
		setCurrentPage(result);
				
		this.lastError = null;
		fireEvent(State.IDLE, ALL);
	}
	
	
	private long offset() {
		R cp = getCurrentPage();		
		return (cp == null) ? 0 : cp.offset();
	}
	
	public Throwable getLastError() {
		return lastError;
	}

	public Fetcher<Q, R, Receiver<R>> getFetcher() {
		return fetcher;
	}

	public void setFetcher(Fetcher<Q, R, Receiver<R>> fetcher) {		
		if (fetcher == null) {
			throw new NullPointerException("fetcher");
		}
		
		this.fetcher = fetcher;
	}

	@Override
	public Q getQuery() {
		return query;
	}

	@Override
	public void setQuery(Q query) {
		if (query == null) {
			throw new NullPointerException("query");
		}
		
		this.query = query;
	}
}

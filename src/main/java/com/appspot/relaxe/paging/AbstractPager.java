/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
		
	private Map<Registration, PagerEventHandler<G>> handlerMap;
		
	private final PagerEvent<G> INDEX = new PagerEvent<G>(self(), Flags.INDEX);  
	private final PagerEvent<G> LOAD_STATE = new PagerEvent<G>(self(), Flags.LOAD_STATE);
	private final PagerEvent<G> LOAD_FAILURE = new PagerEvent<G>(self(), Flags.LOAD_STATE, Flags.LOAD_FAILURE);
	private final PagerEvent<G> ALL = new PagerEvent<G>(self(), Flags.INDEX, Flags.PAGE, Flags.LOAD_STATE);
		
	private int pageSize;	
	private Q query;
		
	private Fetcher<Q, R, PageReceiver<R>> fetcher;
	
	private FetchResultReceiver receiver = null;
		
	private final PageReceiver<Throwable> errorReceiver = new PageReceiver<Throwable>() {		
		@Override
		public void receive(Throwable result) {
			if (receiver != null && receiver.isCanceled()) {
				return;
			}
			
			lastError = result;			
			fireEvent(null, LOAD_FAILURE);
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
	
	public AbstractPager(Fetcher<Q, R, PageReceiver<R>> fetcher, int pageSize, R currentPage) {
		super();		
		setFetcher(fetcher);
		
		if (pageSize < 1) {
			throw new IllegalArgumentException("invalid pageSize: " + pageSize);
		}
		
		this.pageSize = pageSize;
		this.currentPage = currentPage;
	}
	
	public AbstractPager(Fetcher<Q, R, PageReceiver<R>> fetcher, int pageSize, Q query, R currentPage) {
		this(fetcher, pageSize, currentPage);
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
		FetchResultReceiver r = new FetchResultReceiver() {
			@Override
			public void receive(R result) {
				if(!isCanceled()) {				
					received(result, nextIndex);
				}
			}
		};
		
		fireEvent(r, LOAD_STATE);
		
		FetchOptions opts = new FetchOptions(ps, no);
		getFetcher().fetch(getQuery(), opts, r, errorReceiver);
	}
	
	private void fireEvent(FetchResultReceiver newReceiver, PagerEvent<G> event) {
		this.receiver = newReceiver;
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
						
//			public void renew() {
//				getHandlerMap().put(this, handler);
//			}
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
		return (this.receiver == null) ? State.IDLE : State.LOADING;
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
		// return (this.receiver == State.LOADING);
		return (this.receiver != null);
	}
	
	private void setCurrentPage(R currentPage) {
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
		fireEvent(null, ALL);
	}
	
	
	public boolean cancel() {
		if (this.receiver == null) {
			return false;
		}
		
		this.receiver.cancel();
		fireEvent(null, LOAD_STATE);
		
		return true;		
	}
	
	
	public void set(R result) {
		if (this.receiver != null) {
			this.receiver.cancel();
		}		
		
		received(result, 0);		
	}
	
	
	private long offset() {
		R cp = getCurrentPage();		
		return (cp == null) ? 0 : cp.offset();
	}
	
	public Throwable getLastError() {
		return lastError;
	}

	public Fetcher<Q, R, PageReceiver<R>> getFetcher() {
		return fetcher;
	}

	public void setFetcher(Fetcher<Q, R, PageReceiver<R>> fetcher) {		
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
	
	private abstract class FetchResultReceiver 
		implements PageReceiver<R> {
		
		private boolean canceled;
		
		
		public boolean isCanceled() {
			return canceled;
		}
		
		public void cancel() {
			this.canceled = true;			
		}		
	}
}

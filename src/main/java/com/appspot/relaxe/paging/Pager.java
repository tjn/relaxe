/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import com.appspot.relaxe.model.Registration;


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
	boolean isFirstElement();
	boolean isLastElement();	
		
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
		
	public G self();
}

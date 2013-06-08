/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;
import java.util.List;

import com.appspot.relaxe.paging.AbstractPager;
import com.appspot.relaxe.paging.ElementListPage;

public class ListPager<E extends Serializable>
	extends AbstractPager<Void, E, ElementListPage<E>, ListPager<E>> {
	
	public ListPager(List<E> content, int pageSize) {
		this(new ListFetcher<E>(content), pageSize, null);		
	}
	
	public ListPager(ListFetcher<E> fetcher, int pageSize, ElementListPage<E> currentPage) {
		super(fetcher, pageSize, currentPage);
		
	}

	@Override
	public ListPager<E> self() {
		return this;
	}	
}

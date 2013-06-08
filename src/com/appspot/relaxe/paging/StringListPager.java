/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.util.List;

public class StringListPager
	extends ListPager<String> {
	
	public StringListPager(List<String> content, int pageSize) {
		this(new StringListFetcher(content), pageSize);
	}
	
	public StringListPager(StringListFetcher fetcher, int pageSize) {
		super(fetcher, pageSize, null);
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

public class StringListPager
	extends AbstractPager<Void, String, ElementListPage<String>, StringListPager> {
	
	public StringListPager(StringListFetcher fetcher, int pageSize) {
		super(fetcher, pageSize);
	}

	@Override
	public StringListPager self() {
		return this;
	}	
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.util.Map;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.QueryExpressionSource;
import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.paging.DataObjectPager;
import com.appspot.relaxe.paging.DefaultPagerModel;
import com.appspot.relaxe.paging.Fetcher;
import com.appspot.relaxe.paging.PageReceiver;


public class DefaultDataObjectPager
	extends DefaultPagerModel<
		QueryExpressionSource, 
		DataObjectQueryResult<DataObject>, 
		DefaultDataObjectPager, 
		Fetcher<QueryExpressionSource, DataObjectQueryResult<DataObject>, 
		PageReceiver<DataObjectQueryResult<DataObject>>>
	>
	implements DataObjectPager<DataObject, DataObjectQueryResult<DataObject>, DefaultDataObjectPager>
{	
	public DefaultDataObjectPager(
			QueryExpressionSource template,
			Fetcher<QueryExpressionSource, DataObjectQueryResult<DataObject>, PageReceiver<DataObjectQueryResult<DataObject>>> fetcher,
			int initialPageSize, Map<Command, ValueModel<String>> nmm) {
		super(template, fetcher, initialPageSize, nmm);
	}

	@Override
	public DefaultDataObjectPager self() {
		return this;
	}	
}

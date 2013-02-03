/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.util.Map;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.paging.DataObjectPager;
import fi.tnie.db.paging.DefaultPagerModel;
import fi.tnie.db.paging.Fetcher;
import fi.tnie.db.paging.Receiver;

public class DefaultDataObjectPager
	extends DefaultPagerModel<
		QueryExpressionSource, 
		DataObjectQueryResult<DataObject>, 
		DefaultDataObjectPager, 
		Fetcher<QueryExpressionSource, DataObjectQueryResult<DataObject>, 
		Receiver<DataObjectQueryResult<DataObject>>>
	>
	implements DataObjectPager<DataObject, DataObjectQueryResult<DataObject>, DefaultDataObjectPager>
{	
	public DefaultDataObjectPager(
			QueryExpressionSource template,
			Fetcher<QueryExpressionSource, DataObjectQueryResult<DataObject>, Receiver<DataObjectQueryResult<DataObject>>> fetcher,
			int initialPageSize, Map<Command, ValueModel<String>> nmm) {
		super(template, fetcher, initialPageSize, nmm);
	}

	@Override
	public DefaultDataObjectPager self() {
		return this;
	}	
}

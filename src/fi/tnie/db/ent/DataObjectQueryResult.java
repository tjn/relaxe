/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.List;

import fi.tnie.db.query.Query;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.query.QueryTime;

public class DataObjectQueryResult<T extends DataObject>
	extends QueryResult<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4860198226914576207L;
	
	private DataObject.MetaData meta;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DataObjectQueryResult() {
	}
	
	public DataObjectQueryResult(Query request, DataObject.MetaData meta, List<? extends T> content, QueryTime elapsed, FetchOptions options, long offset) {
		super(request, content, elapsed, options, offset);
		this.meta = meta;
	}
		
	public DataObject.MetaData getMeta() {
		return meta;
	}
	
	public T get(int index) {				
		return getContent().get(index);
	}
}

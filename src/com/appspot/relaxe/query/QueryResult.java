/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.query;

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.paging.ResultPage;
import com.appspot.relaxe.rpc.AbstractResponse;



public class QueryResult<T>
	extends AbstractResponse<Query>
	implements ResultPage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3297095270132748103L;
	
	protected QueryResult() {
		super();
	}
	
	private Long available;
	private List<? extends T> content;
	private QueryTime elapsed;
	private FetchOptions options;
	private long offset;
			
	public QueryResult(Query request, List<? extends T> content, QueryTime elapsed, FetchOptions options, long offset) {
		this(request, content);
		this.elapsed = elapsed;		
		this.options = options;
		this.offset = offset;
	}
	
	public QueryResult(Query request, List<? extends T> content) {
		super(request);
		this.content = content;
	}	

	public List<? extends T> getContent() {
		if (content == null) {
			content = new ArrayList<T>();			
		}

		return content;
	}

	public Long getAvailable() {
		return available;
	}

	public void setAvailable(Long available) {
		this.available = available;
	}

	public QueryTime getElapsed() {
		return elapsed;
	}
	
	@Override
	public FetchOptions getFetchOptions() {
		return this.options;
	}

	@Override
	public long getOffset() {
		return offset;
	}
		
	@Override
	public int size() {		
		return (this.content == null) ? 0 : this.content.size();
	}	

	@Override
	public Boolean isLastPage() {		
		Long a = this.available;
		
		if (a == null) {
			return null;
		}
			
		boolean more = a.longValue() > getOffset() + size();
		return Boolean.valueOf(!more);
	}
	
	
	@Override
	public Long available() {
		return getAvailable();
	}
}

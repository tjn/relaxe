/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.ent.im.AbstractResponse;


public class QueryResult<T>
	extends AbstractResponse<Query> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3297095270132748103L;
	
	protected QueryResult() {
		super();
	}
	
	private Long available;	
	
	private List<? extends T> content;
	
	private long queryTime;	 
	private long populationTime;

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

	public long getQueryTime() {
		return queryTime;
	}

	void setQueryTime(long queryTime) {
		this.queryTime = queryTime;
	}

	public long getPopulationTime() {
		return populationTime;
	}

	void setPopulationTime(long populationTime) {
		this.populationTime = populationTime;
	}
	
	public Boolean isComplete() {
		// TODO
		return null;
//		return (available == null) ? null : available.intValue();  
	}

	public Long getAvailable() {
		return available;
	}

	public void setAvailable(Long available) {
		this.available = available;
	}
}

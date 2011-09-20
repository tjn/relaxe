/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.query;

import java.io.Serializable;

public class QueryTime
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7899684211701751541L;
	private long queryExecutionTime;
	private long populationTime;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private QueryTime() {
	}
	
	public QueryTime(long queryExecutionTime) {
		super();
		this.queryExecutionTime = queryExecutionTime;
	}

	public QueryTime(long queryExecutionTime, long populationTime) {
		super();
		this.queryExecutionTime = queryExecutionTime;
		this.populationTime = populationTime;
	}
	
	public long getQueryExecutionTime() {
		return queryExecutionTime;
	}
	
	public long getPopulationTime() {
		return populationTime;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		
		buf.append(super.toString());
		buf.append(": execution=");
		buf.append(this.queryExecutionTime);
		buf.append("ms; population: ");
		buf.append(this.populationTime);
		buf.append("ms");		
		
		return buf.toString();
	}
}

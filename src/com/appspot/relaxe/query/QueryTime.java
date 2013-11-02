/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.query;

import java.io.Serializable;

public class QueryTime
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7899684211701751541L;
	private long executionTime;
	private long populationTime;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private QueryTime() {
	}
	
	public QueryTime(long executionTime) {
		super();
		this.executionTime = executionTime;
	}

	public QueryTime(long executionTime, long populationTime) {
		super();
		this.executionTime = executionTime;
		this.populationTime = populationTime;
	}
	
	public long getExecutionTime() {
		return executionTime;
	}
	
	public long getPopulationTime() {
		return populationTime;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		
		buf.append(super.toString());
		buf.append(": execution=");
		buf.append(this.executionTime);
		buf.append("ms; population: ");
		buf.append(this.populationTime);
		buf.append("ms");		
		
		return buf.toString();
	}
}

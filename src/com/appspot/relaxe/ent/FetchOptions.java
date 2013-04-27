/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

public class FetchOptions
	implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5894929494069252288L;
	private long offset;
	private Adjustment adjustment;
	private Integer count;
	private boolean fetchCardinality;
	
	enum Adjustment {
		NONE,
		AUTO
	}
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected FetchOptions() {
	}
	
	public FetchOptions(int count, long offset) {
		this(Integer.valueOf(count), offset);
	}
	
	/**
	 * Negative offset is interpreted as an offset counting from the end.
	 * 
	 * @param pageSize
	 * @param offset
	 */	
	public FetchOptions(Integer pageSize, long offset) {
		this(pageSize, offset, Adjustment.AUTO, true);
	}
	
	public FetchOptions(Integer count, long offset, Adjustment adjust, boolean cardinality) {
		super();
		this.offset = offset;
		this.count = count;
		this.adjustment = adjust;		
		this.fetchCardinality = cardinality;
	}
	
	public FetchOptions(long offset) {
		super();
		this.offset = offset;
		this.count = null;
		this.adjustment = Adjustment.NONE;
	}


	public long getOffset() {
		return this.offset;
	}
	
	public Adjustment getAdjustment() {
		return adjustment;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public boolean getCardinality() {
		return fetchCardinality;		
	}
}

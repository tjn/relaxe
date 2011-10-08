/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

public class FetchOptions
	implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5894929494069252288L;
	private long offset;
	private Long pageSize;
	private Adjustment adjustment;
	private Long count;
	private boolean cardinality;
	
	enum Adjustment {
		NONE,
		AUTO
	}
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected FetchOptions() {
	}
	
	public FetchOptions(int pageSize, long offset) {
		this(Long.valueOf(pageSize), offset);
	}
	
	/**
	 * Negative offset is interpreted as an offset counting from the end.
	 * 
	 * @param pageSize
	 * @param offset
	 */	
	public FetchOptions(Long pageSize, long offset) {
		this(pageSize, offset, Adjustment.AUTO, null, true);
	}
	
	public FetchOptions(Long pageSize, long offset, Adjustment adjust, Long count, boolean cardinality) {
		super();
		this.offset = offset;
		this.pageSize = pageSize;
		this.adjustment = adjust;
		this.count = count;
		this.cardinality = cardinality;
	}
	
	public FetchOptions(long offset) {
		super();
		this.offset = offset;
		this.pageSize = null;
		this.adjustment = Adjustment.NONE;
	}


	public long getOffset() {
		return this.offset;
	}
	
	public Long getPageSize() {
		return pageSize;
	}
	
	public Adjustment getAdjustment() {
		return adjustment;
	}
	
	public Long getCount() {
		return count;
	}
	
	public boolean getCardinality() {
		return cardinality;		
	}
}

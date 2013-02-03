/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;
import java.util.List;

import fi.tnie.db.ent.FetchOptions;

public class ElementListPage<E extends Serializable>
	implements Page<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1342370951301316614L;
	
	private List<E> content;
	private Long available;
	private long offset;
	private FetchOptions options;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ElementListPage() {
	}
	
	public ElementListPage(List<E> content, long offset, Long available, FetchOptions options) {
		super();
		this.content = content;
		this.offset = offset;
		this.options = options;
		this.available = available;		
	}

	@Override
	public Long available() {
		return this.available;
	}

	@Override
	public List<E> getContent() {	
		return this.content;
	}

	@Override
	public long offset() {
		return this.offset;
	}

	@Override
	public FetchOptions options() {
		return this.options;
	}

}

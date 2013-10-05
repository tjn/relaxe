/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.List;


public class NestedJoin
	extends AbstractTableReference {

	/**
	 * 
	 */
	private static final long serialVersionUID = 660240989210616854L;
	
	private JoinedTable content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NestedJoin() {
	}
	
	public NestedJoin(JoinedTable content) {
		super();
		
		if (content == null) {
			throw new NullPointerException("'content' must not be null");
		}
		
		this.content = content;
	}
	
	@Override
	public void addAll(List<SelectListElement> dest) {
		this.content.addAll(dest);		
	}
		
	@Override
	public ElementList<? extends Identifier> getUncorrelatedColumnNameList() {
		return content.getUncorrelatedColumnNameList();
	}
	
	@Override
	public ElementList<? extends Identifier> getColumnNameList() {
		return getUncorrelatedColumnNameList();
	}

	@Override
	public final OrdinaryIdentifier getCorrelationName(QueryContext qctx) {		
		return null;
	}

	@Override
	public int getColumnCount() {
		return this.content.getColumnCount();
	}

	@Override
	public SelectListElement getAllColumns() {
		return this.content.getAllColumns();
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class SimpleTableRefList 
	extends ElementList<AbstractTableReference>
	implements Element, TableRefList
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1541318396941005713L;

	public SimpleTableRefList() {
		super();
	}
	
	public SimpleTableRefList(AbstractTableReference tref) {
		this();
		getContent().add(tref);
	}

	@Override
	public int getCount() {
		return getContent().size();
	}
	
	@Override
	public AbstractTableReference getItem(int i) {		
		return getContent().get(i);
	}
	
	
}

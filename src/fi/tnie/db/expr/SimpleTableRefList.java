/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class SimpleTableRefList 
	extends ElementList<AbstractTableReference>
	implements Element, TableRefList
{
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

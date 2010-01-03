/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class SimpleTableRefList 
	extends ElementList<AbstractTableReference>
	implements Element
{
	public SimpleTableRefList() {
		super();
	}
	
	public SimpleTableRefList(AbstractTableReference tref) {
		this();
		getContent().add(tref);
	}	
}

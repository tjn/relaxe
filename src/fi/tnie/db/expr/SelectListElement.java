/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.List;

public interface SelectListElement
	extends Element {

	/**
	 * Would something like: 
	 * <code>Attribute getColumnName(int column)</code>  
	 * or
	 * <code>void append(Collection<Attribute> dest)</code>
	 * be better? 
	 * @return
	 */	
	List<? extends ColumnName> getColumnNames();	

	/** Gets the column count.
	 * <code>getColumnCount() = getColumnNames().size()</code> is always true
	 * 
	 * @return
	 */
	public int getColumnCount();
}

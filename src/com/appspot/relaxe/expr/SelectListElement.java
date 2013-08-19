/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

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
	List<? extends Identifier> getColumnNames();
	
	/** 
	 * @param column 1-based index
	 * @return
	 */
	ValueExpression getColumnExpr(int column);
	
	/** 
	 * @param column 1-based index
	 * @return
	 */	
	ColumnExpr getTableColumnExpr(int column);	
	

	/** Gets the column count.
	 * <code>getColumnCount() = getColumnNames().size()</code> is always true
	 * 
	 * @return
	 */
	public int getColumnCount();
}

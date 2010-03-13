/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public interface ValueExpression 
	extends Expression, SelectListElement {
	/**
	 * SQL JavaType of the expression
	 * @return
	 */
	int getType();
	
	/**
	 * Explicit, unambiguous column name of the expression, if available.
	 *  
	 * @return
	 */
	ColumnName getColumnName();
}

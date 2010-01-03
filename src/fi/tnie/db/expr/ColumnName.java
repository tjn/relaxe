/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


/**
 * Represents the symbol of the column in table-reference declaration:
 * 
 * <code>
 * 	SELECT 
 *    * 
 *  FROM
 *  (
 *    ...
 *  )
 *  AS R (<column-symbol>, ...)
 * </code>
 *  
 * @author Administrator
 *
 */

public interface ColumnName extends Element {
	String getColumnName();
}

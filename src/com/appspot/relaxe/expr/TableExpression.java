/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public interface TableExpression
	extends Expression {
	
	/**
	 * Returns the SELECT -clause for this table expression or <code>null</code> if such clause is not currently set.   
	 *  
	 * @return
	 */
	Select getSelect();	
	/**
	 * Returns the FROM -clause for this table expression or <code>null</code> if there is not such clause.   
	 *  
	 * @return
	 */
	From getFrom();
	/**
	 * Returns the WHERE -clause for this table expression or <code>null</code> if there is not such clause.   
	 *  
	 * @return
	 */	
	Where getWhere();
	/**
	 * Returns the GROUP BY -clause for this table expression or <code>null</code> if there is not such clause.   
	 *  
	 * @return
	 */	
	GroupBy getGroupBy();

}

/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.expr;


/**
 * Represents query expression SQL construct.
 * 
 * <code><query-expression> ::= <table-expression> [ <order-by-clause [ limit-clause ] [ limit-clause ] ]</code>
 */
public interface QueryExpression
	extends Expression {

	/**
	 * The table expression of the query expression. 
	 * 
	 * @return
	 */	
	public TableExpression getTableExpr();
	
	/**
	 * The <code>ORDER BY</code> clause of this query expression or <code>null</code> 
	 * 
	 * @return
	 */	
	public OrderBy getOrderBy();
	
	/**
	 * The <code>LIMIT</code> clause of this query expression or <code>null</code> 
	 * 
	 * @return
	 */	
	public Limit getLimit();
	
	/**
	 * The <code>OFFSET</code> clause of this query expression or <code>null</code> 
	 * 
	 * @return
	 */		
	public Offset getOffset();

	String generate();

}
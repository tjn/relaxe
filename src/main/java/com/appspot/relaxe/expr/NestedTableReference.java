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

import java.util.List;


public class NestedTableReference
	extends NonJoinedTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8816662022129528101L;
	private QueryExpression query;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NestedTableReference() {
	}
			
	public NestedTableReference(QueryExpression query) {
		super();
		
		if (query == null) {
			throw new NullPointerException("query");
		}
		
		this.query = query;
	}

	public QueryExpression getQuery() {
		return query;
	}

	@Override
	public ElementList<? extends Identifier> getUncorrelatedColumnNameList() {	
		return getQuery().getTableExpr().getSelect().getColumnNameList();
	}
		
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.getQuery().traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
		getCorrelationClause().traverse(vc, v);
	}

	@Override
	public void addAll(List<SelectListElement> dest) {
		ElementList<SelectListElement> el = getQuery().getTableExpr().getSelect().getSelectList();		
		dest.addAll(el.getContent());
	}

	@Override
	public int getColumnCount() {
		return getQuery().getTableExpr().getSelect().getColumnCount();
	}



	
	
}

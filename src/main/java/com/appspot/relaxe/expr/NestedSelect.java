/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

public class NestedSelect
	extends AbstractQueryExpression	
	implements TableExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1754416875449914000L;
	private QueryExpression inner;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NestedSelect() {
	}

	public NestedSelect(QueryExpression inner) {
		super();
		
		if (inner == null) {
			throw new NullPointerException("'inner' must not be null");
		}
		
		this.inner = inner;
	}	
		
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);		
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.inner.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
		v.end(this);		
	}

	@Override
	public Select getSelect() {
		return this.getTableExpr().getSelect();
	}

	@Override
	public OrderBy getOrderBy() {
		return null;
	}
	
	@Override
	public Limit getLimit() {	
		return null;
	}
	
	@Override
	public Offset getOffset() {	
		return null;
	}
	
	@Override
	public Where getWhere() {
		return null;
	}

	@Override
	public TableExpression getTableExpr() {
		return this.inner.getTableExpr();
	}

	@Override
	public From getFrom() {
		return null;
	}

	@Override
	public GroupBy getGroupBy() {
		return null;
	}	
}

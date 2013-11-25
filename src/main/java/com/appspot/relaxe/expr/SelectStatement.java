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

import com.appspot.relaxe.ent.QueryExpressionSource;


/** 
 * @author tnie
 */
public class SelectStatement
	extends Statement
	implements QueryExpression, QueryExpressionSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 540136231677444030L;
	private TableExpression tableExpression;
	
	private OrderBy orderBy;
	private Limit limit;
	private Offset offset;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SelectStatement() {
	}
	
	public SelectStatement(Select s, From f, Where w) {
		this((TableExpression) new DefaultTableExpression(s, f, w, null));
	}
	
	public SelectStatement(QueryExpression qe) {
		this(qe.getTableExpr(), qe.getOrderBy(), qe.getLimit(), qe.getOffset());
	}
	
	public SelectStatement(TableExpression te) {
		this(te, null, null, null);
	}
	
	public SelectStatement(TableExpression te, OrderBy orderBy) {
		this(te, orderBy, null, null);
	}
		
	public SelectStatement(TableExpression te, OrderBy orderBy, Limit limit, Offset offset) {
		super(Name.SELECT);
		this.tableExpression = te;
		this.orderBy = orderBy;
		this.limit = limit;		
		this.offset = offset;
	}
	
	public TableExpression nest() {
		return new NestedSelect(this);
	}	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		
	
//		getQueryExpression().traverse(vc, v);
		
		getTableExpr().traverse(vc, v);
		
		OrderBy o = getOrderBy();
						
		if (o != null) {
			o.traverse(vc, v);		
		}
		
		Limit limit = getLimit();
		
		if (limit != null) {
			limit.traverse(vc, v);
			
			Offset off = getOffset();
			
			if (off != null) {
				off.traverse(vc, v);
			}			
		}
	}

	@Override
	public Limit getLimit() {
		return limit;
	}

	@Override
	public Offset getOffset() {
		return offset;
	}
	@Override
	public QueryExpression getQueryExpression() {
		return this;
	}
	
	@Override
	public OrderBy getOrderBy() {
		return this.orderBy;
	}
	
	@Override
	public TableExpression getTableExpr() {
		return this.tableExpression;
	}
}

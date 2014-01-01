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

import com.appspot.relaxe.expr.op.BinaryOperator;

public class SetOperator
	extends BinaryOperator
	implements TableExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1520583086311992257L;
	private Op operator;
	private SQLKeyword all;
	private TableExpression left;
	private TableExpression right;
	
	public enum Op {
		UNION(SQLKeyword.UNION),
		INTERSECT(SQLKeyword.INTERSECT),
		EXCEPT(SQLKeyword.EXCEPT),
		;
		
		private SQLKeyword name;
		
		private Op(SQLKeyword name) {
			this.name = name;
		}
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SetOperator() {
	}

	public SetOperator(Op operator, boolean all, TableExpression left, TableExpression right) {
		super(operator.name, left, right);
		
		if (left == null) {
			throw new NullPointerException("'left' must not be null");
		}
		
		if (right == null) {
			throw new NullPointerException("'right' must not be null");
		}
		
		this.operator = operator;
		this.left = left;
		this.right = right;
		this.all = all ? SQLKeyword.ALL : null;
	}

	public boolean isAll() {
		return this.all != null;
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		
		v.start(vc, this);
				
		left.traverse(vc, v);
		operator.name.traverse(vc, v);
		
		if (this.all != null) {
			this.all.traverse(vc, v);						
		}
		
		right.traverse(vc, v);
		
		v.end(this);
	}
		
	@Override
	public Select getSelect() {		
		return this.left.getSelect();
	}
	
	@Override
	public Where getWhere() {
		return null;
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

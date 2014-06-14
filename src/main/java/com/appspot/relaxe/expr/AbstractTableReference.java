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



public abstract class AbstractTableReference
	extends CompoundElement
	implements TableRefList {
								
	/**
	 * 
	 */
	private static final long serialVersionUID = -712700693670065896L;

	public AbstractTableReference() {
		super();
	}
	
	public abstract OrdinaryIdentifier getCorrelationName(QueryContext qctx);
	
	public AbstractTableReference innerJoin(AbstractTableReference right, JoinCondition jc) {
		return new JoinedTable(this, right, JoinType.INNER, jc);
	}
		
	public AbstractTableReference leftJoin(AbstractTableReference right, JoinCondition jc) {
		return new JoinedTable(this, right, JoinType.LEFT, jc);
	}	
	
	/**
	 * List of uncorrelated column names.
	 * 
	 * @return
	 */
	protected abstract ElementList<Identifier> getUncorrelatedColumnNameList();
		
	/**
	 * List of (possibly) correlated column names.
	 * 
	 * @return
	 */	
	public abstract ElementList<Identifier> getColumnNameList();
		
//	/**
//	 * 
//	 * @return
//	 */
//	public abstract ElementList<ValueElement> getSelectList();

//	protected void copyElementList(AbstractTableReference src, ElementList<ValueElement> dest) {
//		if (src != null) {
//			src.getSelectList().copyTo(dest);
//		}
//	}
	
	public abstract int getColumnCount();
	
	// public abstract void addAll(ElementList<SelectListElement> dest);
	public abstract void addAll(List<SelectListElement> dest);

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		traverseContent(vc, v);
		v.end(this);		
	}

	@Override
	public final int getCount() {
		return 1;
	}

	@Override
	public AbstractTableReference getItem(int i) {		
		if (i == 0) {
			return this;
		}
		
		throw new IndexOutOfBoundsException("expected 0, actual: " + i);		
	}	

	public abstract SelectListElement getAllColumns();
}

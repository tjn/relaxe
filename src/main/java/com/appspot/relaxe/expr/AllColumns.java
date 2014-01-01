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

import java.util.ArrayList;
import java.util.List;

public abstract class AllColumns 
	implements SelectListElement, Token {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7004307634380311562L;
	private Symbol allColumns = Symbol.ASTERISK;	
	
	public AllColumns() {
		super();
	}

	@Override
	public List<? extends Identifier> getColumnNames() {
		TableRefList refs = getTableRefs(); 
		
		if (refs == null) {
			return null;
		}	
		
		int rc = refs.getCount();
		
		if (rc == 1) {
			return refs.getItem(0).getColumnNameList().getContent();
		}
		
		List<Identifier> nl = new ArrayList<Identifier>();
		
		for (int i = 0; i < rc; i++) {
			nl.addAll(refs.getItem(i).getColumnNameList().getContent());
		}
		
		return nl;
	}
	


	@Override
	public String getTerminalSymbol() {
		return allColumns.getTerminalSymbol();
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		allColumns.traverse(vc, v);
	}

	@Override
	public boolean isOrdinary() {
		return this.allColumns.isOrdinary();
	}

	@Override
	public int getColumnCount() {
		int cc = 0;
		
		TableRefList refs = getTableRefs();
				
		int rc = refs.getCount();
		
		for (int i = 0; i < rc; i++) {
			AbstractTableReference tref = refs.getItem(i);
			cc += tref.getColumnCount();			
		}
		
		return cc;
	}
	
	@Override
	public ValueExpression getColumnExpr(final int column) {
		return getTableColumnExpr(column);
	}
	
	
	@Override
	public ColumnExpr getTableColumnExpr(final int column) {
		int c = 0;	
		ColumnExpr e = null;
		
		int index = column - 1;
		
		if (index < 0 || index >= getColumnCount()) {
			throw new IndexOutOfBoundsException("column=" + column);
		}
		
		TableRefList refs = getTableRefs();				
		int rc = refs.getCount();
		
		for (int i = 0; i < rc; i++) {
			AbstractTableReference tref = refs.getItem(i);
			c += tref.getColumnCount();
			
			if (column <= c) {
				int pos = c - column;				
				e = tref.getAllColumns().getTableColumnExpr(pos);
				break;
			}
		}
		
		return e;	
	}

	protected abstract TableRefList getTableRefs();
}

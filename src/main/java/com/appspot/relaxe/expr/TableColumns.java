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

/**
 * @author Administrator
 *
 */
public class TableColumns
	extends CompoundElement
	implements SelectListElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8004995546047752690L;
	private NonJoinedTable tableRef;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected TableColumns() {
	}

	public TableColumns(NonJoinedTable tableRef) {
		super();
		
		if (tableRef == null) {
			throw new NullPointerException("'tableRef' must not be null");
		}
		
		this.tableRef = tableRef;
	}

	@Override
	public List<Identifier> getColumnNames() {	
		ElementList<Identifier> names = tableRef.getColumnNameList();
		
		List<Identifier> nl = new ArrayList<Identifier>(names.size());
		
		for (Identifier n : names) {
			nl.add(n);
		}
		
		return nl;
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {		
		v.start(vc, this);
		
		OrdinaryIdentifier cn = tableRef.getCorrelationName(v.getContext());				
		cn.traverse(vc, v);
		Symbol.DOT.traverse(vc, v);
		Symbol.ASTERISK.traverse(vc, v);
		
		v.end(this);
	}

	@Override
	public int getColumnCount() {
//		return tableRef.getColumnNameList().getContent().size();
		return tableRef.getColumnNameList().size();
	}
	
	@Override
	public ValueExpression getColumnExpr(int column) {		
//		return getTableColumnExpr(column);
		int cc = getColumnCount();
		int index = column - 1;
		
		if (index < 0 || index >= cc) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
			 
		return tableRef.getAllColumns().getColumnExpr(column);				
	}

//	@Override
//	public ColumnExpr getTableColumnExpr(int column) {
//		int cc = getColumnCount();
//		int index = column - 1;
//		
//		if (index < 0 || index >= cc) {
//			throw new IndexOutOfBoundsException(Integer.toString(column));
//		}
//			 
//		return tableRef.getAllColumns().getTableColumnExpr(column);
//	}		
}

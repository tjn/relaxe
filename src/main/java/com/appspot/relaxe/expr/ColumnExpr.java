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

public abstract class ColumnExpr 
	extends CompoundElement 
	implements ValueExpression, SelectListElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6471819124970538933L;
	private AbstractTableReference table;	
	private Identifier columnName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ColumnExpr() {
	}

	public ColumnExpr(AbstractTableReference table, Identifier columnName) {
		super();
		this.table = table;		
		this.columnName = columnName;
	}

	public AbstractTableReference getTable() {
		return this.table;
	}
	
	@Override
	public Identifier getColumnName() {
		return columnName;
	}

	@Override
	public int getColumnCount() {	
		return 1;
	}

	@Override
	public ValueExpression getColumnExpr(int column) {
		return getTableColumnExpr(column);
	}

	@Override
	public ColumnExpr getTableColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}

		return this;
	}
	
}

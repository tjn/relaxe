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

import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.meta.Column;


public class ColumnReference
	extends CompoundElement 
	implements ValueExpression, SelectListElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1430117431795245443L;
	
	private AbstractTableReference table;	
	private Identifier columnName;
	private int dataType; 
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public ColumnReference() {
	}
	
	public ColumnReference(AbstractTableReference table, Column column) {
		this(table, column.getColumnName(), column.getDataType().getDataType());
	}
		
	public ColumnReference(AbstractTableReference table, Identifier column, int dataType) {
		this.table = table;
		this.columnName = column;
		this.dataType = dataType;
//		logger().debug("table-col-expr column-name: " + column.getColumnName().getName());
	}
	
	@Override
	public int getType() {				
		return dataType;
	}

	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		AbstractTableReference tref = getTable();
						
		if (tref != null) {
			Identifier cn = tref.getCorrelationName(v.getContext());
			
			if (cn != null) {			
//				logger().debug("corr. name: " + cn);			
				cn.traverse(vc, v);
				Symbol.DOT.traverse(vc, v);
			}
		}
				
		getColumnName().traverse(vc, v);
	}
	
	
	@Override
	public ValueExpression getColumnExpr(int column) {
//		return getTableColumnExpr(column);
		
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
	
		return this;		
	}

//	@Override
//	public ColumnExpr getTableColumnExpr(int column) {
//		if (column != 1) {
//			throw new IndexOutOfBoundsException(Integer.toString(column));
//		}
//
//		return this;
//	}

	
	@Override
	public List<Identifier> getColumnNames() {		
		return Collections.singletonList(getColumnName());
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
	public ValueExpression asValueExpression() {
		return this;
	}
	@Override
	public SQLKeyword asDefaultSpecification() {
		return null;
	}
	
	@Override
	public SQLKeyword asNullSpecification() {
		return null;
	}

}

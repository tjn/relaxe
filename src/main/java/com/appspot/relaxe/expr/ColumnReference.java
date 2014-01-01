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
	extends ColumnExpr 
	implements ValueExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1430117431795245443L;
	
	private Column column;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public ColumnReference() {
	}
		
	public ColumnReference(AbstractTableReference table, Column column) {
		super(table, column.getColumnName());
		
		this.column = column;
//		logger().debug("table-col-expr column-name: " + column.getColumnName().getName());
	}
	
	@Override
	public int getType() {				
		return getColumn().getDataType().getDataType();
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
	
	
	public Column getColumn() {		
		return this.column;
	}
	
	@Override
	public List<? extends Identifier> getColumnNames() {		
		return Collections.singletonList(getColumnName());
	}

}

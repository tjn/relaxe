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

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Table;


public class TableReference 
	extends NonJoinedTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3388366557843780574L;
	private Table table;	
	private ElementList<Identifier> columnNameList;
	
	private SchemaElementName tableName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected TableReference() {
	}
		
	public TableReference(Table table) {
		super();
		
		if (table == null) {
			throw new NullPointerException("'table' must not be null");
		}
		
		this.table = table;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getTableName().traverse(vc, v);		
		getCorrelationClause().traverse(vc, v);
	}
	
	public Table getTable() {
		return table;
	}

	@Override
	public ElementList<? extends Identifier> getUncorrelatedColumnNameList() {
		if (columnNameList == null) {
			this.columnNameList = new ElementList<Identifier>();
			
			List<Identifier> nl = this.columnNameList.getContent();
			
			for (final Column c : getTable().columnMap().values()) {
				nl.add(c.getColumnName());
			}
		}
		
		return columnNameList;
	}
		
	public SchemaElementName getTableName()
		throws IllegalIdentifierException {
		if (tableName == null) {
//			tableName = new SchemaElementName(getTable());
			SchemaElementName sen = getTable().getName();
			tableName = sen;
		}

		return tableName;
	}

	@Override
	public void addAll(List<SelectListElement> dest) {
		for (Column	c : getTable().columnMap().values()) {
			dest.add(new ColumnReference(this, c));
		}
	}
	
	@Override
	public int getColumnCount() {		
		return getTable().columnMap().size();
	}
}

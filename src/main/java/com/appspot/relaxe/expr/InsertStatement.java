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

import java.util.Collection;

import com.appspot.relaxe.meta.Table;

public class InsertStatement
	extends SQLDataChangeStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1598811041382537945L;

	private ElementList<Identifier> columnNameList;
	private TableExpression source;
	private SchemaElementName tableName;
	
	
	
	public int getTargetColumnCount() {
		return columnNameList.size();
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected InsertStatement() {
	}
	
	private InsertStatement(Table target, ElementList<Identifier> columnNameList) {
		super(Name.INSERT);
		
		if (target == null) {
			throw new NullPointerException("'target' must not be null");
		}
		
		this.tableName = target.getName().withoutCatalog();		
		this.columnNameList = columnNameList;
	}
	
	
	/**
	 * Constructs a new  
	 * 
	 * @param target Target table to insert rows into.
	 * @param columnNameList Must not be null.
	 * @param valueRow May me null.
	 */
	public InsertStatement(Table target, ElementList<Identifier> columnNameList, TableExpression source) {
		this(target, columnNameList);
		this.source = source;
	}
	

	
	/**
	 * Constructs a new  
	 * 
	 * @param target Target table to insert rows into.
	 * @param columnNameList Must not be null.
	 * @param valueRow May me null.
	 */
	public InsertStatement(Table target, ElementList<Identifier> columnNameList, RowValueConstructor valueRow) {
		this(target, columnNameList, TableValueConstructor.of(valueRow));
	}
		
	public InsertStatement(Table target, ElementList<Identifier> columnNameList, Collection<RowValueConstructor> rows) {
		this(target, columnNameList, ElementList.newElementList(rows));
	}
	
	public InsertStatement(Table target, ElementList<Identifier> columnNameList, ElementList<RowValueConstructor> rows) {
		this(target, columnNameList, TableValueConstructor.of(rows));
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		SQLKeyword.INSERT.traverse(vc, v);
		SQLKeyword.INTO.traverse(vc, v);
								
		this.tableName.traverse(vc, v);
		
		ElementList<Identifier> cl = getColumnNameList();
		
		if (cl != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);	
			cl.traverse(vc, v);
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
		
		this.source.traverse(vc, v);
	}

	public TableExpression getSource() {
		return source;
	}
	
	public SchemaElementName getTableName() {
		return this.tableName;
	}
	
	protected ElementList<Identifier> getColumnNameList() {
		return columnNameList;
	}
	
	
}

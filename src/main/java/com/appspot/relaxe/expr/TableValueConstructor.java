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


public class TableValueConstructor<E extends Element>
	extends CompoundElement
		implements TableExpression {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -4615610962973762991L;
	private E content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	private TableValueConstructor() {
	}
	
	private TableValueConstructor(E content) {		
		this.content = content;
	}
		
	public static TableValueConstructor<RowValueConstructor> of(RowValueConstructor row) {
		return new TableValueConstructor<RowValueConstructor>(row);
	}
	
	public static TableValueConstructor<ElementList<RowValueConstructor>> of(ElementList<RowValueConstructor> rows) {
		return new TableValueConstructor<ElementList<RowValueConstructor>>(rows);
	}
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.VALUES.traverse(vc, v);
		this.content.traverse(vc, v);		
	}
	
	@Override
	public Select getSelect() {
		return null;
	}
	
	@Override
	public From getFrom() {
		return null;
	}
	
	@Override
	public Where getWhere() {
		return null;
	}
		
	@Override
	public GroupBy getGroupBy() {
		return null;
	}	
}

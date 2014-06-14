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


public class NestedJoin
	extends AbstractTableReference {

	/**
	 * 
	 */
	private static final long serialVersionUID = 660240989210616854L;
	
	private JoinedTable content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NestedJoin() {
	}
	
	public NestedJoin(JoinedTable content) {
		super();
		
		if (content == null) {
			throw new NullPointerException("'content' must not be null");
		}
		
		this.content = content;
	}
	
	@Override
	public void addAll(List<SelectListElement> dest) {
		this.content.addAll(dest);		
	}
		
	@Override
	public ElementList<Identifier> getUncorrelatedColumnNameList() {
		return content.getUncorrelatedColumnNameList();
	}
	
	@Override
	public ElementList<Identifier> getColumnNameList() {
		return getUncorrelatedColumnNameList();
	}

	@Override
	public final OrdinaryIdentifier getCorrelationName(QueryContext qctx) {		
		return null;
	}

	@Override
	public int getColumnCount() {
		return this.content.getColumnCount();
	}

	@Override
	public SelectListElement getAllColumns() {
		return this.content.getAllColumns();
	}
}

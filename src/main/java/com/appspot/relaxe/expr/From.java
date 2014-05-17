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

public class From extends AbstractClause {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3675394470595921573L;
	private TableRefList tableReferenceList;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected From() {
	}
	
	public From(TableReference tref) {
		this(new SimpleTableRefList(tref));
	}
	
	public From(TableRefList from) {
		if (from == null) {
			throw new NullPointerException("'from' must not be null");
		}
		
		tableReferenceList = from;
	}

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("FROM ");
//		getTableReferenceList().generate(qc, dest);
//	}

	public TableRefList getTableReferenceList() {
		return tableReferenceList;
	}

//	public void setTableReferenceList(TableRefList tableReferenceList) {
//		this.tableReferenceList = tableReferenceList;
//	}

	@Override
	protected Element getContent() {		
		return getTableReferenceList();
	}
	
	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		SQLKeyword.FROM.traverse(vc, v);		
	}
	
}

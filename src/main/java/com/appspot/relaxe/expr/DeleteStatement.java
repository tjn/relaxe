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

public class DeleteStatement
	extends SQLDataChangeStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7983755125216586175L;

	private TableReference target;	
	private Where where;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DeleteStatement() {
	}
	
	public DeleteStatement(TableReference tableReference, Predicate p) {
		super(Name.DELETE);
		
		if (tableReference == null) {
			throw new NullPointerException("tableReference");
		}
		
		this.target = tableReference;
		this.where = (p == null) ? null : new Where(p);
	}
		
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.DELETE.traverse(vc, v);		
		SQLKeyword.FROM.traverse(vc, v);
		getTarget().traverse(vc, v);		
		
		if (getWhere() != null) {
			getWhere().traverse(vc, v);
		}
	}

	public TableReference getTarget() {
		return target;
	}	

	public Where getWhere() {
		return where;
	}
}

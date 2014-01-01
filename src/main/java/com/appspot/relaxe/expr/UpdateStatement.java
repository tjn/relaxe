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

public class UpdateStatement
	extends SQLDataChangeStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042322606478948245L;
	private TableReference target;			
	private ElementList<Assignment> assignmentClause;
	private Where where;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected UpdateStatement() {
	}
				
	public UpdateStatement(TableReference tref, ElementList<Assignment> assignmentClause, Predicate p) {
		super(Name.UPDATE);
		
		if (tref == null) {
			throw new NullPointerException("'tref' must not be null");
		}
		
		if (assignmentClause == null) {
			throw new NullPointerException("'assignmentClause' must not be null");
		}		
		
		this.target = tref;
		this.assignmentClause = assignmentClause;		
		this.where = (p == null) ? null : new Where(p);
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.UPDATE.traverse(vc, v);		
		getTarget().traverse(vc, v);		
		SQLKeyword.SET.traverse(vc, v);
		this.assignmentClause.traverse(vc, v);
		
		if (getWhere() != null) {
			getWhere().traverse(vc, v);
		}		
	}

	public TableReference getTarget() {
		return target;
	}	
	
	public ElementList<Assignment> getAssignmentList() {
		if (assignmentClause == null) {
			assignmentClause = new ElementList<Assignment>();			
		}

		return assignmentClause;
	}

	
	public Where getWhere() {
		return this.where;
	}
}

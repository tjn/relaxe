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
package com.appspot.relaxe.env.mysql;

import com.appspot.relaxe.expr.DeleteStatement;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.OrdinaryIdentifier;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.VisitContext;

public class MySQLDeleteStatement
	extends DeleteStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2492700739356278591L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected MySQLDeleteStatement() {
	}
	
	public MySQLDeleteStatement(TableReference tref, Predicate p) {
		super(tref, p);		
	}		
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		// DELETE FROM R USING <schema>.<table> R WHERE ...
		
		SQLKeyword.DELETE.traverse(vc, v);		
		SQLKeyword.FROM.traverse(vc, v);		
		TableReference tref = getTarget();
		OrdinaryIdentifier cn = tref.getCorrelationName(v.getContext());
		cn.traverse(vc, v);		
		MySQLKeyword.USING.traverse(vc, v);				
		tref.traverse(vc, v);	
		getWhere().traverse(vc, v);
	}
}

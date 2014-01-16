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
package com.appspot.relaxe.expr.pg;

import com.appspot.relaxe.expr.AbstractClause;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.ValueRow;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.Table;

public class PGInsert
	extends InsertStatement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4505440690619367270L;
	private ReturningClause returning;
	
	public PGInsert() {
		super();
	}
	
	public PGInsert(Table target) {
		super(target);
	}

	public PGInsert(Table target, ElementList<Identifier> columnNameList, ValueRow valueRow) {
		super(target, columnNameList, valueRow);
	}

	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {

		SQLKeyword.INSERT.traverse(vc, v);
		SQLKeyword.INTO.traverse(vc, v);
			
		getTableName().traverse(vc, v);		
		
		ElementList<Identifier> cl = getColumnNameList();
		
		if (cl != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);	
			cl.traverse(vc, v);
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
		
		ElementList<ValueRow> vr = getValues();
		
		if (vr.isEmpty()) {
			SQLKeyword.DEFAULT.traverse(vc, v);
			SQLKeyword.VALUES.traverse(vc, v);
		}
		else {
			SQLKeyword.VALUES.traverse(vc, v);		
			getValues().traverse(vc, v);			
		}		
		
		ReturningClause rc = getReturning();
		
		if (rc != null) {
			rc.traverse(vc, v);
		}
	}

	public ReturningClause getReturning() {
		return returning;
	}

	public void setReturning(ReturningClause returning) {
		this.returning = returning;
	}

	public static class ReturningClause
		extends AbstractClause {
			
		/**
		 * 
		 */
		private static final long serialVersionUID = -7588547880513689368L;
//		private ValueElement e;
	
		public ReturningClause() {			
		}
	
		@Override
		protected Element getContent() {	
			return null;
		}

		@Override
		protected void traverseClause(VisitContext vc, ElementVisitor v) {
			PostgreSQLKeyword.RETURNING.traverse(vc, v);
		}
	}
	


}

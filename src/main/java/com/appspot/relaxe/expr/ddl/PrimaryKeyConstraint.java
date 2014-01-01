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
package com.appspot.relaxe.expr.ddl;

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.expr.CompoundElement;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.PrimaryKey;

public class PrimaryKeyConstraint
	extends CompoundElement
	implements BaseTableElement {
	
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1949817230870504185L;
	
	private Identifier constraintName;
	private ElementList<Identifier> columnList;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private PrimaryKeyConstraint() {
	}
	
	public PrimaryKeyConstraint(Identifier constraintName, List<Identifier> columnList) {
		this.constraintName = constraintName;		
		this.columnList = new ElementList<Identifier>(columnList);
	}
	
	public PrimaryKeyConstraint(PrimaryKey key) {
		this.constraintName = key.getUnqualifiedName();
		
		ColumnMap cm = key.getColumnMap();
		
		int cc = cm.size();
		List<Identifier> nl = new ArrayList<Identifier>(cc);
		
		for (int i = 0; i < cc; i++) {
			nl.add(cm.get(i).getUnqualifiedName());
		}
				
		this.columnList = new ElementList<Identifier>(nl);
	}	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {	
		if (constraintName != null) {
			SQLKeyword.CONSTRAINT.traverse(vc, v);
			constraintName.traverse(vc, v);
		}
		
		SQLKeyword.PRIMARY.traverse(vc, v);
		SQLKeyword.KEY.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		columnList.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
	}
}

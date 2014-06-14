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
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;

public class ForeignKeyJoinCondition 
	extends JoinCondition { 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4566688892782028829L;
	private ForeignKey foreignKey;
	private AbstractTableReference referencing;		
	private AbstractTableReference referenced;
	
	private Predicate condition;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private ForeignKeyJoinCondition() {
	}
	
	public ForeignKeyJoinCondition(ForeignKey foreignKey, AbstractTableReference referencing, AbstractTableReference referenced) {
		super();
		this.foreignKey = foreignKey;
		this.referencing = referencing; 
		this.referenced = referenced;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getCondition().traverse(vc, v);
	}

	private Predicate getCondition() {
		if (this.condition == null) {
			Predicate jp = null;
			
			ColumnMap cm = foreignKey.getColumnMap();
			
			for (Column a : cm.values()) {				
				Column b = foreignKey.getReferenced(a);
				Comparison p = Comparison.eq(new ColumnReference(referencing, a), new ColumnReference(referenced,  b));
				jp = AndPredicate.newAnd(jp, p);				
			}
			
			
//			for (Entry<Column, Column> e : foreignKey.column().entrySet()) {
//				Column a = e.getKey();				
//				Column b = e.getValue();
//							
//				jp = AndPredicate.newAnd(jp, Comparison.eq(
//						new ColumnReference(referencing, a),
//						new ColumnReference(referenced,  b)));
//			}
			
			this.condition = jp;

		}

		return this.condition;		
	}
	
	
	
	
}
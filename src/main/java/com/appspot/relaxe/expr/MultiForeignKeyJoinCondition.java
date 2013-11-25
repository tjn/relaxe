/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import java.util.Map;

import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;

public class MultiForeignKeyJoinCondition 
	extends JoinCondition { 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4566688892782028829L;
	private AbstractTableReference referencing;
	
	private Map<ForeignKey, TableReference> foreignKeyReferenceMap;
				
	private Predicate condition;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private MultiForeignKeyJoinCondition() {
	}
	
	public MultiForeignKeyJoinCondition(AbstractTableReference referencing, Map<ForeignKey, TableReference> foreignKeyReferenceMap) {
		super();
		this.referencing = referencing;
		this.foreignKeyReferenceMap = foreignKeyReferenceMap;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getCondition().traverse(vc, v);
	}

	private Predicate getCondition() {
		if (this.condition == null) {
			Predicate jp = null;
			
			for (Map.Entry<ForeignKey, TableReference> e : foreignKeyReferenceMap.entrySet()) {
				ForeignKey key = e.getKey();
				AbstractTableReference ref = e.getValue();
				
				for (Column a : key.getColumnMap().values()) {									
					Column b = key.getReferenced(a);
					
					Comparison pc = Comparison.eq(
							new ColumnReference(referencing, a),
							new ColumnReference(ref,  b));
								
					jp = AndPredicate.newAnd(jp, pc);
				}								
			}
			
			this.condition = jp;
		}

		return this.condition;		
	}	
}
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

import java.util.List;
import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;

public class CompoundJoinCondition 
	extends JoinCondition { 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4566688892782028829L;
	
	public static class Component {
		private TableReference referencing;
		private TableReference referenced;
		private ForeignKey key;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		protected Component() {	
		}
				
		
		public Component(TableReference referencing, ForeignKey key, TableReference referenced) {
			super();
			this.referencing = referencing;			
			this.key = key;
			this.referenced = referenced;
		}

		public TableReference getReferencing() {
			return referencing;
		}
		
		public TableReference getReferenced() {
			return referenced;
		}		
		
		public ForeignKey getKey() {
			return key;
		}
	}
	
//	private Map<ForeignKey, TableReference> foreignKeyReferenceMap;
	
	private List<Component> componentList;	
				
	private Predicate condition;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private CompoundJoinCondition() {
	}
	
//	public CompoundJoinCondition(AbstractTableReference referencing, Map<ForeignKey, TableReference> foreignKeyReferenceMap) {
//		super();
//		this.referencing = referencing;
//		this.foreignKeyReferenceMap = foreignKeyReferenceMap;
//	}
	
	public CompoundJoinCondition(List<Component> cl) {
		super();
		this.componentList = cl;
	}

	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getCondition().traverse(vc, v);
	}

	private Predicate getCondition() {
		if (this.condition == null) {
			Predicate jp = null;
			
			
			for (Component c : this.componentList) {
				ForeignKey key = c.getKey();
				TableReference t1 = c.getReferencing();
				TableReference t2 = c.getReferenced();
				
				for (Column a : key.getColumnMap().values()) {									
					Column b = key.getReferenced(a);
					
					Comparison pc = Comparison.eq(
							new ColumnReference(t1, a),
							new ColumnReference(t2, b));
								
					jp = AndPredicate.newAnd(jp, pc);
				}				
			}
			
			this.condition = jp;
		}

		return this.condition;		
	}
}
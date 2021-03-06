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
package com.appspot.relaxe.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.appspot.relaxe.expr.Identifier;

public class ImmutableForeignKey
	extends AbstractImmutableForeignKey
	implements ForeignKey {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311988355706864990L;
		
	private BaseTable referenced;
	
	private ImmutableForeignKey() {
		super();	
	}

	private ImmutableForeignKey(BaseTable table, BaseTable referenced, Identifier name, ColumnMap columnMap, Map<Identifier, Identifier> columnPairMap) {
		super(table, name, columnMap, columnPairMap);
		this.referenced = referenced;
	}

	public static class Builder
		extends ImmutableConstraint.Builder<ForeignKey> {
		
		private BaseTable referenced; 
		private List<Identifier> columnList;
		private Map<Identifier, Identifier> columnPairMap;
		private Identifier constraintName;		
		
		public Builder(Identifier constraintName, BaseTable referencing, BaseTable referenced) {
			super(referencing);
			
			if (constraintName == null) {
				throw new NullPointerException("constraintName");
			}
			
			this.constraintName = constraintName;			
			this.columnList = new ArrayList<Identifier>();
			this.columnPairMap = createMap();
			this.referenced = referenced;
		}
		
		public boolean add(Identifier a, Identifier b) {
			if (a == null) {
				throw new NullPointerException("a");
			}
			
			if (b == null) {
				throw new NullPointerException("b");
			}
			
			if (this.columnPairMap.containsKey(a)) {
				return false;
			}
						
			this.columnList.add(a);
			this.columnPairMap.put(a, b);
			
			return true;
		}				
		
		@Override
		public Identifier getConstraintName() {
			return this.constraintName;
		}
		
		void setReferenced(BaseTable referenced) {
			this.referenced = referenced;
		}
		
		public BaseTable getReferenced() {
			return referenced;
		}
				
		protected ImmutableForeignKey newConstraint()
			throws ElementInstantiationException {
			
			if (this.columnList.isEmpty()) {
				return null;
			}
												
			ImmutableColumnMap.Builder cmb = new ImmutableColumnMap.Builder(getEnvironment());
						
			final BaseTable table = getTable();
						
			for (Identifier n : this.columnList) {
				Column column = table.getColumnMap().get(n);
				
				if (column == null) {
					throw new ElementInstantiationException("no column: " + n);
				}
				
				cmb.add(column);
			}
						
			ColumnMap cm = cmb.newColumnMap();
			
			ImmutableForeignKey fk = new ImmutableForeignKey(table, getReferenced(), constraintName, cm, this.columnPairMap);
							
			return fk;
		}
	}
	
//	@Override
//	public Column getReferenced(Column referencingColumn) {
//		if (referencingColumn == null) {
//			return null;
//		}
//				
//		Identifier cn = getReferencedColumnName(referencingColumn.getUnqualifiedName());
//		ColumnMap cm = getReferenced().columnMap();
//		Column rc = cm.get(cn);
//		return rc;
//	}

	
	@Override
	public BaseTable getReferenced() {
		return this.referenced;
	}
}

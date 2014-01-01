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

import com.appspot.relaxe.expr.Identifier;

public class ImmutablePrimaryKey
	extends ImmutableConstraint
	implements PrimaryKey {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311988355706864990L;
		
	private ColumnMap columnMap;

	@Override
	public ColumnMap getColumnMap() {
		return this.columnMap;
	}
	
	
	private ImmutablePrimaryKey() {
		super();	
	}

	private ImmutablePrimaryKey(BaseTable table, Identifier name, ColumnMap columnMap) {
		super(table, name);
		this.columnMap = columnMap;
	}


	@Override
	public Type getType() {
		return Type.PRIMARY_KEY;
	}

	
	public static class Builder
		extends ImmutableConstraint.Builder<ImmutablePrimaryKey> {

		private Identifier constraintName;
		private ImmutableColumnMap.Builder columnMapBuilder;
		
		public Builder(BaseTable table) {
			super(table);
			this.columnMapBuilder = new ImmutableColumnMap.Builder(table.getEnvironment());
		}
		
		public void add(Column c) {
			this.columnMapBuilder.add(c);
		}
		
		public ImmutablePrimaryKey newConstraint() {			
			if (this.columnMapBuilder.getColumnCount() == 0) {
				return null;
			}	
			
			ColumnMap cm = this.columnMapBuilder.newColumnMap();
			ImmutablePrimaryKey pk = new ImmutablePrimaryKey(this.getTable(), constraintName, cm);
							
			return pk;
		}

		@Override
		public Identifier getConstraintName() {
			return constraintName;
		}

		public void setConstraintName(Identifier constraintName) {
			this.constraintName = constraintName;
		}
	}
}

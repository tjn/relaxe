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
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;

public abstract class ImmutableConstraint
	extends ImmutableSchemaElement
	implements Constraint {	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1563851898050957700L;
	
	
	private BaseTable table;
		
	public BaseTable getTable() {
		return this.table;
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableConstraint() {
	}	
	
	protected ImmutableConstraint(BaseTable table, Identifier name) {
		super(table.getEnvironment(), new SchemaElementName(table.getName().getQualifier(), name));
		this.table = table;
	}
	
	public static abstract class Builder<C extends Constraint>
		extends MetaObjectBuilder {
		
		private BaseTable table;		
		
		protected Builder(BaseTable table) {
			super(table.getEnvironment());
			this.table = table;
		}
		
		public BaseTable getTable() {
			return table;
		}	

		public abstract Identifier getConstraintName();
	}
}

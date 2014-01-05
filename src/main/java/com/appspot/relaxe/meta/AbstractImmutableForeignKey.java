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

import java.util.Map;

import com.appspot.relaxe.expr.Identifier;

public abstract class AbstractImmutableForeignKey
	extends ImmutableConstraint
	implements ForeignKey {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311988355706864990L;
	
	private ColumnMap columnMap;
	private Map<Identifier, Identifier> columnPairMap;
	
	protected AbstractImmutableForeignKey() {
		super();	
	}

	protected AbstractImmutableForeignKey(BaseTable table, Identifier name, ColumnMap columnMap, Map<Identifier, Identifier> columnPairMap) {
		super(table, name);	
		this.columnMap = columnMap;	
		this.columnPairMap = columnPairMap;
	}

	@Override
	public Type getType() {
		return Type.FOREIGN_KEY;
	}
	
	@Override
	public Identifier getReferencedColumnName(Identifier referencingColumn) {
		if (referencingColumn == null) {
			return null;
		}
		
		return this.columnPairMap.get(referencingColumn);		
	}
	
	@Override
	public Column getReferenced(Column referencingColumn) {
		if (referencingColumn == null) {
			return null;
		}
				
		Identifier cn = getReferencedColumnName(referencingColumn.getUnqualifiedName());
		ColumnMap cm = getReferenced().getColumnMap();
		Column rc = cm.get(cn);
		return rc;
	}


	@Override
	public ColumnMap getColumnMap() {
		return this.columnMap;
	}
	
	@Override
	public abstract BaseTable getReferenced();


	@Override
	public BaseTable getReferencing() {
		return getTable();
	}
}

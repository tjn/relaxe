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
package com.appspot.relaxe.ent.query;

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElementTag;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.meta.Column;

public class EntityQueryColumnReference
	implements EntityQueryValue {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8919013660144056879L;
	
	private EntityQueryElementTag element;
	private Identifier columnName;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private EntityQueryColumnReference() {
	}
		
	public EntityQueryColumnReference(EntityQueryElementTag element, Identifier columnName) {
		super();
		
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		if (columnName == null) {
			throw new NullPointerException("columnName");
		}
		
		this.element = element;
		this.columnName = columnName;
		
	}
	
	@Override
	public ValueExpression expression(EntityQueryContext c) {
		TableReference tref = c.getTableRef(element);
		
		Column column = tref.getTable().getColumnMap().get(columnName);
		
		if (column == null) {
			throw new NullPointerException("no column " + columnName.toString() + " in table " + tref.getTable().getQualifiedName());
		}
		
		ColumnReference ref = new ColumnReference(tref, column);		
		
		return ref;
	}	
}

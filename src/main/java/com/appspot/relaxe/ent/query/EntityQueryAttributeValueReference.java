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
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.meta.Column;

public class EntityQueryAttributeValueReference<
	A extends com.appspot.relaxe.ent.Attribute,
	QE extends EntityQueryElement<A, ?, ?, ?, ?, ?, ?, QE>
>
	implements EntityQueryValueReference {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8919013660144056879L;
	
	private QE element;
	private A attribute;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private EntityQueryAttributeValueReference() {
	}
		
	public EntityQueryAttributeValueReference(QE element, A attribute) {
		super();
		
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}
		
		this.element = element;
		this.attribute = attribute;
		
	}
	
	@Override
	public ValueExpression expression(EntityQueryContext c) {
		TableReference tref = c.getTableRef(element);
		
		if (tref == null) {
			throw new NullPointerException();
		}
		
		Column column = this.element.getMetaData().getColumn(this.attribute);
		ColumnReference ref = new ColumnReference(tref, column);		
		
		return ref;
	}	
}

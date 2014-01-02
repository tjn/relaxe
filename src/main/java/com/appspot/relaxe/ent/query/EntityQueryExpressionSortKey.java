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

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.OrderBy;
import com.appspot.relaxe.expr.OrderBy.ExprSortKey;
import com.appspot.relaxe.expr.OrderBy.Order;
import com.appspot.relaxe.expr.OrderBy.SortKey;
import com.appspot.relaxe.expr.ValueExpression;

public abstract class EntityQueryExpressionSortKey
	implements EntityQuerySortKey
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6187737730779858706L;
	
	private EntityQueryValueReference value;
	
	public EntityQueryExpressionSortKey(EntityQueryValueReference value) {
		super();
		
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		this.value = value;		
	}	
	
	public <
		A extends com.appspot.relaxe.ent.Attribute,
		M extends EntityMetaData<A, ?, ?, ?, ?, ?, M>
	>	
	EntityQueryExpressionSortKey(EntityQueryElement<A, ?, ?, ?, ?, ?, M, ?> element, A attribute) {
		super();
		
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}
				
		M meta = element.getMetaData();
		Identifier col = meta.getColumn(attribute).getColumnName();		
		this.value = new EntityQueryColumnReference(element, col);
	}
	

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected EntityQueryExpressionSortKey() {
	}

	@Override
	public SortKey sortKey(EntityQueryContext ctx) {
		ValueExpression ve = this.value.expression(ctx);		
		ExprSortKey k = new OrderBy.ExprSortKey(ve, order());
		return k;
	}
	
	public abstract OrderBy.Order order();

	public static class Asc
		extends EntityQueryExpressionSortKey {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7207325066731675954L;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Asc() {	
		}

		public <
			A extends Attribute, 
			M extends EntityMetaData<A, ?, ?, ?, ?, ?, M>
		> 
		Asc(
			EntityQueryElement<A, ?, ?, ?, ?, ?, M, ?> element, A attribute) {
			super(element, attribute);		
		}
				
		@Override
		public Order order() {
			return OrderBy.Order.ASC;
		}		
	}
	
	public static class Desc
		extends EntityQueryExpressionSortKey {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1670068397185184796L;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Desc() {	
		}

		public <
			A extends Attribute, 
			M extends EntityMetaData<A, ?, ?, ?, ?, ?, M>
		> 
		Desc(
			EntityQueryElement<A, ?, ?, ?, ?, ?, M, ?> element, A attribute) {
			super(element, attribute);		
		}


		@Override
		public Order order() {
			return OrderBy.Order.DESC;
		}		
	}	
}

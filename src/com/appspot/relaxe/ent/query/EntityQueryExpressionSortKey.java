/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
		M extends EntityMetaData<A, ?, ?, ?, ?, ?, M, ?>
	>	
	EntityQueryExpressionSortKey(EntityQueryElement<A, ?, ?, ?, ?, ?, M, ?, ?> element, A attribute) {
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
			M extends EntityMetaData<A, ?, ?, ?, ?, ?, M, ?>
		> 
		Asc(
			EntityQueryElement<A, ?, ?, ?, ?, ?, M, ?, ?> element, A attribute) {
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
			M extends EntityMetaData<A, ?, ?, ?, ?, ?, M, ?>
		> 
		Desc(
			EntityQueryElement<A, ?, ?, ?, ?, ?, M, ?, ?> element, A attribute) {
			super(element, attribute);		
		}


		@Override
		public Order order() {
			return OrderBy.Order.DESC;
		}		
	}	
}

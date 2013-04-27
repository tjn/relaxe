/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.OrderBy;
import com.appspot.relaxe.expr.OrderBy.SortKey;


public class EntityQueryExpressionSortKey<A extends Attribute>
	implements EntityQuerySortKey<A>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6187737730779858706L;
	
	private OrderBy.SortKey sortKey;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected EntityQueryExpressionSortKey() {
	}

	public EntityQueryExpressionSortKey(SortKey sortKey) {
		super();
		this.sortKey = sortKey;
	}
	
	@Override
	public SortKey sortKey(ColumnReference cr) {
		return this.sortKey;
	}	
	
	public static <A extends Attribute> EntityQuerySortKey<A> newSortKey(OrderBy.SortKey expr) {
		return new EntityQueryExpressionSortKey<A>(expr);
	}

	@Override
	public A attribute() {
		return null;
	}
}

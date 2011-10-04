/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.OrderBy.SortKey;


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

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.OrderBy.Order;
import fi.tnie.db.expr.OrderBy.SortKey;

/**
 * Provides a EntityQueryTemplateAttribute implementation with the capability to act as an EntityQuerySortKey.
 * 
 *  
 *  
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public abstract class SortKeyAttributeTemplate<A extends Attribute>
	implements EntityQuerySortKey<A> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3079799118698355746L;
	
	private A attribute;
	
	public static <A extends Attribute> SortKeyAttributeTemplate<A> get(A a, OrderBy.Order so) {
		so = (so == null) ? Order.ASC : so;			
		return (so == Order.ASC) ? asc(a) : desc(a);
	}
		
	public static <A extends Attribute> SortKeyAttributeTemplate<A> asc(A attribute) {
		return new Asc<A>(attribute);
	}
	
	public static <A extends Attribute> SortKeyAttributeTemplate<A> desc(A attribute) {
		return new Desc<A>(attribute);
	}
	
	protected SortKeyAttributeTemplate() {
	}
		
	public SortKeyAttributeTemplate(A attribute) {
		super();
		this.attribute = attribute;
	}



	public static class Asc<A extends Attribute>
		extends SortKeyAttributeTemplate<A> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4545159706806621845L;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Asc() {	
		}

		protected Asc(A attribute) {
			super(attribute);
		}
		
		@Override
		public Order sortOrder() {
			return Order.ASC;
		}
	}

	
	public static class Desc<A extends Attribute>
		extends SortKeyAttributeTemplate<A> {
				
		/**
		 * 
		 */
		private static final long serialVersionUID = 4575239762087833095L;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Desc() {	
		}		
		
		protected Desc(A attribute) {
			super(attribute);
		}

		@Override
		public Order sortOrder() {
			return Order.DESC;
		}
	}


	@Override
	public SortKey sortKey(ColumnReference cref) {
		return new OrderBy.ExprSortKey(cref, sortOrder());
	}		

	
	public abstract OrderBy.Order sortOrder();
	
	
	@Override
	public A attribute() {
		return this.attribute;
	}	
}

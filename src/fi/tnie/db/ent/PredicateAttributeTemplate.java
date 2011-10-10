/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.IntLiteral;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.op.Comparison;
import fi.tnie.db.types.PrimitiveType;

/**
 *  
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public abstract class PredicateAttributeTemplate<A extends Attribute>
	implements EntityQueryPredicate<A> {
	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4009167582842494268L;
	private A attribute;
	
	protected PredicateAttributeTemplate() {
	}
		
	public PredicateAttributeTemplate(A attribute) {
		super();
		this.attribute = attribute;
	}


	public static class Null<A extends Attribute>
		extends PredicateAttributeTemplate<A> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5183112198249471108L;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Null() {	
		}
		
		public Null(A attribute) {
			super(attribute);			
		}
				
		@Override
		public Predicate predicate(ColumnReference cr) {
			return new fi.tnie.db.expr.op.IsNull(cr);
		}		
	}

		
	public static class Equals<A extends Attribute>
		extends PredicateAttributeTemplate<A> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4545159706806621845L;		
		private ValueExpression expression;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Equals() {	
		}
		
		public Equals(A attribute, ValueExpression ve) {
			super(attribute);
			this.expression = ve;
		}

		public 
		<
			M extends EntityMetaData<A, ?, ?, ?, ?, ?, M>
		>
		Equals(M meta, A attribute, ValueExpression ve) {
			this(attribute, ve);
			
			PrimitiveType<?> pt = meta.getAttributeType(attribute);
			
			if (pt.getSqlType() != ve.getType()) {
				throw new IllegalArgumentException("type mismatch: " + attribute + ": " + pt.getSqlType() + "; " + ve.getType());
			}			
		}
				
		@Override
		public Predicate predicate(ColumnReference cr) {
			return Comparison.eq(cr, this.expression);			
		}
	}


		@Override
	public abstract Predicate predicate(ColumnReference cr);
	
	
	@Override
	public A attribute() {
		return this.attribute;
	}
	
	public static 
	<A extends Attribute>
	PredicateAttributeTemplate<A> eq(A attribute, Integer value) {
		if (value == null) {
			return new Null<A>(attribute);
		}
		else {
			return new Equals<A>(attribute, new IntLiteral(value.intValue()));
		}		
	}
}

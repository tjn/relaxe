/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.IntLiteral;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.StringLiteral;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.op.Comparison;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.VarcharHolder;
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
		public Predicate predicate(TableReference tref, ColumnReference cr) {
			return new fi.tnie.db.expr.op.IsNull(cr);
		}		
	}

	public static class NotNull<A extends Attribute>
		extends PredicateAttributeTemplate<A> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5183112198249471108L;
	
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private NotNull() {	
		}
		
		public NotNull(A attribute) {
			super(attribute);			
		}
				
		@Override
		public Predicate predicate(TableReference tref, ColumnReference cr) {
			return new fi.tnie.db.expr.op.IsNotNull(cr);
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
			M extends EntityMetaData<A, ?, ?, ?, ?, ?, M, ?>
		>
		Equals(M meta, A attribute, ValueExpression ve) {
			this(attribute, ve);
			
			PrimitiveType<?> pt = meta.getAttributeType(attribute);
			
			if (pt.getSqlType() != ve.getType()) {
				throw new IllegalArgumentException("type mismatch: " + attribute + ": " + pt.getSqlType() + "; " + ve.getType());
			}			
		}
				
		@Override
		public Predicate predicate(TableReference tref, ColumnReference cr) {
			return Comparison.eq(cr, this.expression);			
		}
	}


		@Override
	public abstract Predicate predicate(TableReference tref, ColumnReference cr);
	
	
	@Override
	public A attribute() {
		return this.attribute;
	}
	
	
	public static 
	<A extends Attribute>
	PredicateAttributeTemplate<A> isNull(A attribute) {
		return new Null<A>(attribute);
	}
	
	public static 
	<A extends Attribute>
	PredicateAttributeTemplate<A> isNotNull(A attribute) {
		return new NotNull<A>(attribute);
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
	
	public static 
	<A extends Attribute>
	PredicateAttributeTemplate<A> eq(A attribute, String value) {
		if (value == null) {
			return new Null<A>(attribute);
		}
		else {
			return new Equals<A>(attribute, new StringLiteral(value));
		}		
	}
	
	
	public static 
	<
		A extends Attribute,
		E extends Entity<A, ?, ?, E, ?, ?, ?, ?>
	>
	PredicateAttributeTemplate<A> eq(E e, IntegerKey<A, ?, E> k) {
		IntegerHolder h = k.get(e);		
		return eq(k.name(), h.value());
	}
	
	public static 
	<
		A extends Attribute,
		E extends Entity<A, ?, ?, E, ?, ?, ?, ?>
	>
	PredicateAttributeTemplate<A> eq(E e, VarcharKey<A, ?, E> k) {
		VarcharHolder h = k.get(e);		
		return eq(k.name(), h.value());
	}
	
	
	public static 
	<
		A extends Attribute,
		K extends PrimitiveKey<A, ?, ?, ?, ?, ?, K>
	>
	PredicateAttributeTemplate<A> isNotNull(K k) {
		return new NotNull<A>(k.name());		
	}
	
	public static 
	<
		A extends Attribute,
		K extends PrimitiveKey<A, ?, ?, ?, ?, ?, K>
	>
	PredicateAttributeTemplate<A> isNull(K k) {
		return new Null<A>(k.name());		
	}	
}

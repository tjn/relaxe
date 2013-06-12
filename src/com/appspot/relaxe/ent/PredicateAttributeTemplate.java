/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;


import java.util.Collection;

import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasString;
import com.appspot.relaxe.ent.value.HasVarchar;
import com.appspot.relaxe.ent.value.IntegerKey;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.ent.value.VarcharKey;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.StringLiteral;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.expr.op.ValueExpressionIn;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.types.PrimitiveType;

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
			return new com.appspot.relaxe.expr.op.IsNull(cr);
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
			return new com.appspot.relaxe.expr.op.IsNotNull(cr);
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
			
			PrimitiveType<?> pt = attribute.type();			
			
			if (pt.getSqlType() != ve.getType()) {
				throw new IllegalArgumentException("type mismatch: " + attribute + ": " + pt.getSqlType() + "; " + ve.getType());
			}			
		}
				
		@Override
		public Predicate predicate(TableReference tref, ColumnReference cr) {
			return Comparison.eq(cr, this.expression);			
		}
	}
	
	public static class In<A extends Attribute>
		extends PredicateAttributeTemplate<A> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -4545159706806621845L;		
		private Collection<ValueExpression> values;
	
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private In() {	
		}
		
		public In(A attribute, Collection<ValueExpression> values) {
			super(attribute);
			this.values = values;
		}		
				
		@Override
		public Predicate predicate(TableReference tref, ColumnReference cr) {			
			ValueExpressionIn in = new ValueExpressionIn(cr, values);
			return in;			
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
	
	public static <
		A extends Attribute
	>
	PredicateAttributeTemplate<A> in(A attribute, Collection<ValueExpression> values) {		
		return new In<A>(attribute, values);
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
		E extends HasInteger<A, E>
	>
	PredicateAttributeTemplate<A> eq(E e, IntegerKey<A, E> k) {
		IntegerHolder h = k.get(e);		
		return eq(k.name(), h.value());
	}
	
	
//	public static 
//	<
//		A extends Attribute,
//		E extends HasInteger<A, E>
//	>
//	PredicateAttributeTemplate<A> in(IntegerKey<A, E> k, E e) {
//		IntegerHolder h = k.get(e);		
//		return eq(k.name(), h.value());
//	}
	
	public static 
	<
		A extends Attribute,
		E extends HasVarchar<A, E> & HasString<A, E>
	>
	PredicateAttributeTemplate<A> eq(E e, VarcharKey<A, E> k) {
		VarcharHolder h = k.get(e);		
		return eq(k.name(), h.value());
	}
	
	
	public static 
	<
		A extends Attribute,
		K extends PrimitiveKey<A, ?, ?, ?, ?, K>
	>
	PredicateAttributeTemplate<A> isNotNull(K k) {
		return new NotNull<A>(k.name());		
	}
	
	public static 
	<
		A extends Attribute,
		K extends PrimitiveKey<A, ?, ?, ?, ?, K>
	>
	PredicateAttributeTemplate<A> isNull(K k) {
		return new Null<A>(k.name());		
	}	
}

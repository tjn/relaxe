/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.query;

import java.util.Collection;

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.NotPredicate;
import com.appspot.relaxe.expr.op.OrPredicate;

public class EntityQueryPredicates {
	
	public static class IsNull
		implements EntityQueryPredicate {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1798695033991850362L;
		
		private EntityQueryValueReference value;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private IsNull() {
		}
		
		public IsNull(EntityQueryValueReference value) {
			super();
			
			if (value == null) {
				throw new NullPointerException("value");
			}
			
			this.value = value;
		}
	
		@Override
		public Predicate predicate(EntityQueryContext ctx) {
			ValueExpression ve = value.expression(ctx);		
			return new com.appspot.relaxe.expr.op.IsNull(ve);		
		}
	}
	
	public static class Not
		implements EntityQueryPredicate {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1798695033991850362L;
		
		private EntityQueryPredicate predicate;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Not() {
		}
		
		public Not(EntityQueryPredicate predicate) {
			if (predicate == null) {
				throw new NullPointerException("predicate");
			}
			
			this.predicate = predicate;
		}
	
		@Override
		public Predicate predicate(EntityQueryContext ctx) {
			Predicate p = this.predicate.predicate(ctx);		
			return new NotPredicate(p);		
		}
	}
	
	
	public static class IsNotNull
		implements EntityQueryPredicate {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3714022645688851659L;
		private EntityQueryValueReference value;
			
		/**
		 * No-argument constructor for GWT Serialization
		 */	
		@SuppressWarnings("unused")
		private IsNotNull() {
		}
		
		public IsNotNull(EntityQueryValueReference value) {
			super();
			
			if (value == null) {
				throw new NullPointerException("value");
			}
			
			this.value = value;
		}
	
		@Override
		public Predicate predicate(EntityQueryContext ctx) {
			ValueExpression ve = value.expression(ctx);		
			return new com.appspot.relaxe.expr.op.IsNotNull(ve);		
		}
	}
	
	
	protected static abstract class BooleanOperator
		implements EntityQueryPredicate {

		private EntityQueryPredicate[] operands;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8296321824752469934L;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		protected BooleanOperator() {	
		}
		
		public BooleanOperator(EntityQueryPredicate left, EntityQueryPredicate right) {
			this.operands = new EntityQueryPredicate[] {
				left, right	
			};
		}
		
		public BooleanOperator(Collection<EntityQueryPredicate> operands) {
			super();
			
			if (operands == null) {
				throw new NullPointerException("operands");
			}
			
			if (operands.isEmpty()) {
				throw new IllegalArgumentException("at least one operand is required");
			}			
			
			int i = 0;
			this.operands = new EntityQueryPredicate[operands.size()];
						
			for (EntityQueryPredicate ep : operands) {
				this.operands[i++] = ep;				
			}
		}
		
		public BooleanOperator(EntityQueryPredicate[] operands) {
			super();
			
			if (operands == null) {
				throw new NullPointerException("operands");
			}
			
			if (operands.length == 0) {
				throw new IllegalArgumentException("at least one operand is required");
			}			
			
			this.operands = new EntityQueryPredicate[operands.length];
			System.arraycopy(operands, 0, this.operands, 0, this.operands.length);
		}

		@Override
		public Predicate predicate(EntityQueryContext ctx) {
			int count = this.operands.length;
						
			EntityQueryPredicate ep = this.operands[0];
			Predicate p = ep.predicate(ctx);
			
			for (int i = 1; i < count; i++) {
				ep = this.operands[i];
				Predicate np = ep.predicate(ctx);
				p = newPredicate(p, np);
			}
			
			return p;
		}

		protected abstract Predicate newPredicate(Predicate p, Predicate np);
		
	}
	

	public static class Or
		extends BooleanOperator	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4615656104394696630L;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Or() {	
		}
		
		public Or(EntityQueryPredicate left, EntityQueryPredicate right) {
			super(left, right);
		}
				
		public Or(Collection<EntityQueryPredicate> operands) {
			super(operands);		
		}

		public Or(EntityQueryPredicate[] operands) {
			super(operands);
		}

		@Override
		protected Predicate newPredicate(Predicate a, Predicate b) {
			return new OrPredicate(a, b);
		}
		
		

	}


	public static class And
		extends BooleanOperator	{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6731381593548394794L;

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private And() {	
		}
		
		public And(EntityQueryPredicate left, EntityQueryPredicate right) {
			super(left, right);
		}
		
		public And(EntityQueryPredicate[] operands) {
			super(operands);
		}
		
		public And(Collection<EntityQueryPredicate> operands) {
			super(operands);
		}
	
		@Override
		protected Predicate newPredicate(Predicate a, Predicate b) {
			return new AndPredicate(a, b);
		}
	}
}

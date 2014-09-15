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

import java.util.Collection;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.expr.AbstractRowValueConstructor;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.expr.op.NotPredicate;
import com.appspot.relaxe.expr.op.OrPredicate;
import com.appspot.relaxe.expr.op.ParenthesizedPredicate;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public class EntityQueryPredicates {
	
	public static class IsNull
		implements EntityQueryPredicate {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1798695033991850362L;
		
		private EntityQueryValue value;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private IsNull() {
		}
		
		public IsNull(EntityQueryValue value) {
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
		private EntityQueryValue value;
			
		/**
		 * No-argument constructor for GWT Serialization
		 */	
		@SuppressWarnings("unused")
		private IsNotNull() {
		}
		
		public IsNotNull(EntityQueryValue value) {
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
			
			return new ParenthesizedPredicate(p);
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
	
	
	public static <
		A extends AttributeName,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,		
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>,
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
	>	
	EntityQueryPredicate newIn(QE left, K key, Collection<RE> entities) {
		EntityQueryPredicate inp = new EntityQueryInPredicate.Referenced<A, R, T, E, B, H, F, M, QE, RA, RR, RT, RE, RB, RH, RF, RM, K>(left, key, entities);
		return inp;
	}
	
	
	public static <
		A extends AttributeName,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,		
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,	
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		QE extends EntityQueryElement<RA, RR, RT, RE, RB, RH, RF, RM, QE>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
	>	
	EntityQueryPredicate newInReferencing(QE left, K key, Collection<E> entities) {
		EntityQueryPredicate inp = new EntityQueryInPredicate.Referencing<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, QE, K>(
				left, key, entities);
		
		return inp;
	}
	
	public static <
		A extends AttributeName,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,		
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,	
		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
	>	
	EntityQueryPredicate newIn(QE left, Collection<E> entities) {				
		EntityQueryPredicate inp = new EntityQueryInValuesPredicate<A, R, T, E, B, H, F, M, QE>(left, entities);
		return inp;
	}	
		
	public static EntityQueryPredicate newNot(EntityQueryPredicate inner) {
		return new EntityQueryPredicates.Not(inner);
	}
	
	public static EntityQueryPredicate newAnd(EntityQueryPredicate a, EntityQueryPredicate b) {
		if (a == null) {
			return b;
		}
		
		if (b == null) {
			return a;
		}		
		
		return new EntityQueryPredicates.And(a, b);
	}
	
	public static EntityQueryPredicate newAnd(EntityQueryPredicate a, EntityQueryPredicate ... rest) {
		if (rest == null || rest.length == 0) {
			return a;
		}
		
		EntityQueryPredicate out = a;
		
		for (EntityQueryPredicate p : rest) {
			out = newAnd(out, p);
		}
		
		return out;
	}
	
	public static EntityQueryPredicate newOr(EntityQueryPredicate a, EntityQueryPredicate b) {
		if (a == null) {
			return b;
		}
		
		if (b == null) {
			return a;
		}		
		
		return new EntityQueryPredicates.Or(a, b);
	}
	
	public static EntityQueryPredicate newOr(EntityQueryPredicate a, EntityQueryPredicate ... rest) {
		if (rest == null || rest.length == 0) {
			return a;
		}
		
		EntityQueryPredicate out = a;
		
		for (EntityQueryPredicate p : rest) {
			out = newOr(out, p);
		}
		
		return out;
	}	
	
	
	public static EntityQueryPredicate newIsNull(EntityQueryValue v) {
		return new EntityQueryPredicates.IsNull(v);
	}	
	
	public static EntityQueryPredicate newNotNull(EntityQueryValue v) {
		return new EntityQueryPredicates.IsNotNull(v);
	}	

	public static EntityQueryPredicate newIsNull(EntityReference<?, ?, ?, ?, ?, ?, ?, ?> v) {
//		AbstractRowValueConstructor.		
//		return new EntityQueryPredicates.IsNull(v);
		return null;
	}	
	

	
	public static <
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	EntityQueryPredicate newEquals(EntityReference<A, R, T, E, B, H, F, M> lhs, EntityReference<A, R, T, E, B, H, F, M> rhs) {
		return newComparison(Comparison.Op.EQ, lhs, rhs); 
	}
	
	
	public static <
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	EntityQueryPredicate newEquals(EntityReference<A, R, T, E, B, H, F, M> lhs, E rhs) {		
		return newComparison(Comparison.Op.EQ, lhs, new EntityValue<A, R, T, E, B, H, F, M>(rhs)); 
	}
	
	public static <
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	EntityQueryPredicate newComparison(Comparison.Op operator, EntityReference<A, R, T, E, B, H, F, M> lhs, EntityReference<A, R, T, E, B, H, F, M> rhs) {		
		return new DefaultEntityQueryEntityPredicate(operator, lhs, rhs);		
	}	
	
}

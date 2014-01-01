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
package com.appspot.relaxe.ent;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.appspot.relaxe.ent.query.DefaultEntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQueryAttributeValueReference;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQueryPredicates;
import com.appspot.relaxe.ent.query.EntityQueryValueExpression;
import com.appspot.relaxe.ent.query.EntityQueryValueReference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.expr.op.Comparison.Op;
import com.appspot.relaxe.log.DefaultLogger;
import com.appspot.relaxe.log.Logger;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;

public abstract class DefaultEntityQueryElement<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends com.appspot.relaxe.ent.Content,		
	QE extends EntityQueryElement<A, R, T, E, H, F, M, C, QE>
> 
	implements EntityQueryElement<A, R, T, E, H, F, M, C, QE>	
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5505364328412305185L;
	
		
//	private transient TableReference tableReference;
	
	private Set<EntityQueryPredicate> predicates = new HashSet<EntityQueryPredicate>();
					
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultEntityQueryElement() {
	}
	
	
	@Override
	public Collection<EntityQueryPredicate> predicates() {
		return predicates;
	}
		

	@Override
	public abstract EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> getQueryElement(EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k);
		
	
//	public abstract static class DefaultBuilder<
//		A extends com.appspot.relaxe.ent.Attribute,
//		R extends com.appspot.relaxe.ent.Reference,
//		T extends ReferenceType<A, R, T, E, H, F, M, C>,
//		E extends Entity<A, R, T, E, H, F, M, C>,
//		H extends ReferenceHolder<A, R, T, E, H, M, C>,
//		F extends EntityFactory<E, H, M, F, C>,		
//		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
//		C extends com.appspot.relaxe.ent.Content,	
//		QE extends EntityQueryElement<A, R, T, E, H, F, M, C, QE>
//	>
//		implements EntityQueryElement.Builder<A, R, T, E, H, F, M, C, QE>
//	{
//		
//		private Map<>
//		
//		@Override
//		public EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> getQueryElement(
//				EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k) {		
//			return null;
//		}
//		
//		@Override
//		public <RA extends Attribute, RR extends Reference, RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>, RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>, RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>, RF extends EntityFactory<RE, RH, RM, RF, RC>, RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>, RC extends Content, RQ extends EntityQueryElement<RA, RR, RT, RE, RH, RF, RM, RC, RQ>, K extends EntityKey<com.appspot.relaxe.gen.sakila.ent.sakila.Film.Attribute, com.appspot.relaxe.gen.sakila.ent.sakila.Film.Reference, Type, Film, Holder, Factory, MetaData, com.appspot.relaxe.gen.sakila.ent.sakila.Film.Content, RA, RR, RT, RE, RH, RF, RM, RC, K>> void setQueryElement(
//				K key, QE queryElement) {
//					
//		}
//	}
	
	@Override
	public <
		K extends PrimitiveKey<A, E, ?, ?, ?, K>
	> 
	EntityQueryPredicate newEquals(K key, EntityQueryValueReference rhs) {
		EntityQueryAttributeValueReference<A, QE> lhs = new EntityQueryAttributeValueReference<A, QE>(self(), key.name());
		return new DefaultEntityQueryPredicate(Comparison.Op.EQ, lhs, rhs);
	}
	
	@Override
	public <
		XV extends Serializable, 
		XT extends PrimitiveType<XT>, 
		XH extends PrimitiveHolder<XV, XT, XH>, 
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	> 
	EntityQueryPredicate newNull(K key) {
		EntityQueryAttributeValueReference<A, QE> ref = new EntityQueryAttributeValueReference<A, QE>(self(), key.name());
		return new EntityQueryPredicates.IsNull(ref);
	}


	@Override
	public <
		XV extends Serializable, 
		XT extends PrimitiveType<XT>, 
		XH extends PrimitiveHolder<XV, XT, XH>, 
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	> 
	EntityQueryPredicate newNotNull(K key) {
		EntityQueryAttributeValueReference<A, QE> ref = new EntityQueryAttributeValueReference<A, QE>(self(), key.name());
		return new EntityQueryPredicates.IsNotNull(ref);
	}
	
//	@Override
//	public <
//		XV extends Serializable, 
//		XT extends PrimitiveType<XT>, 
//		XH extends PrimitiveHolder<XV, XT, XH>, 
//		K extends PrimitiveKey<A, E, XV, XT, XH, K>
//	> 
//	EntityQueryPredicate newPredicate(K key, Op op, EntityQueryValueReference rhs) {
//		EntityQueryAttributeValueReference<A, QE> lhs = new EntityQueryAttributeValueReference<A, QE>(self(), key.name());
//		return new DefaultEntityQueryPredicate(op, lhs, rhs);
//	}
	

//	private static Logger logger() {
//		return DefaultEntityQuery.logger;
//	}
	

//	private List<Predicate> getPredicateList() {
//		if (predicateList == null) {
//			predicateList = new ArrayList<Predicate>();
//			
//		}
//
//		return predicateList;
//	}
	

	@Override
	public <
		XV extends Serializable, 
		XT extends PrimitiveType<XT>, 
		XH extends PrimitiveHolder<XV, XT, XH>, 
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	> 
	EntityQueryPredicate newEquals(K key, XH rhs) {
		return newPredicate(key, Comparison.Op.EQ, rhs);
	}
	
	@Override
	public <
		XV extends Serializable, 
		XT extends PrimitiveType<XT>, 
		XH extends PrimitiveHolder<XV, XT, XH>, 
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	>
	EntityQueryPredicate newPredicate(K key, Op op, XH value) {				
		M meta = getMetaData();
		Column column = meta.getColumn(key.name());
		ImmutableValueParameter<XV, XT, XH> vp = new ImmutableValueParameter<XV, XT, XH>(column, value);
		EntityQueryValueExpression rhs = new EntityQueryValueExpression(vp);		
		return newPredicate(key, op, rhs);
	}
	
	@Override
	public <
		XV extends Serializable, 
		XT extends PrimitiveType<XT>, 
		XH extends PrimitiveHolder<XV, XT, XH>, 
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	> 
	EntityQueryPredicate newEquals(K key, XV value) {				
		return newEquals(key, key.newHolder(value));
	}

	
	@Override
	public <
		K extends PrimitiveKey<A, E, ?, ?, ?, K>
	>
	EntityQueryPredicate newPredicate(K key, Comparison.Op op, EntityQueryValueReference rhs) {		
		EntityQueryAttributeValueReference<A, QE> lhs = new EntityQueryAttributeValueReference<A, QE>(self(), key.name());		
		return new DefaultEntityQueryPredicate(op, lhs, rhs);
	}
	
	
	public Logger logger() {
		return DefaultLogger.getLogger();
	}
}


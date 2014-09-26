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
import java.util.Set;

import com.appspot.relaxe.ent.query.EntityReference;
import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQuerySortKey;
import com.appspot.relaxe.ent.query.EntityQueryValue;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;

public interface EntityQueryElement<
	A extends com.appspot.relaxe.ent.AttributeName,
	R extends com.appspot.relaxe.ent.Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,		
	M extends EntityMetaData<A, R, T, E, B, H, F, M>,
	QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
>
	extends Request, EntityQueryElementTag
{	
	/**
	 * result item meta-data
	 * @return
	 */
	M getMetaData();
	
	public int getElementCount();
		
	public Set<A> attributes();
		
	Collection<EntityQueryPredicate> predicates();
		
	public EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> getQueryElement(EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k);
	
	
	QE self();
	
	
	public interface Builder<
		A extends com.appspot.relaxe.ent.AttributeName,
		R extends com.appspot.relaxe.ent.Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
	> {
		
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> getQueryElement(EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k);
		
		public 
		<			
			RA extends com.appspot.relaxe.ent.AttributeName,
			RR extends com.appspot.relaxe.ent.Reference,	
			RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
			RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
			RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
			RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
			RF extends EntityFactory<RE, RB, RH, RM, RF>,
			RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,			
			RQ extends EntityQueryElement<RA, RR, RT, RE, RB, RH, RF, RM, RQ>,
			K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>	
		>
		void setQueryElement(K key, RQ queryElement);
						
		void addAllAttributes();
		
		void add(A attribute);		
		void remove(A attribute);
		
		void add(com.appspot.relaxe.ent.value.Attribute<A, E, ?, ?, ?, ?, ?> key);
		void remove(com.appspot.relaxe.ent.value.Attribute<A, E, ?, ?, ?, ?, ?> key);
		
		void removeAll(Collection<A> attribute);
						
		QE newQueryElement();
	}
	
	/**
	 * 
	 * @param column
	 * @return
	 */
	
//	public TableReference getOrigin(int column)
//		throws EntityException;
//	
//	public EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tr)
//		throws EntityException;
	
//	@Override
//	public TableReference getReferenced(TableReference referencing, ForeignKey fk)
//		throws EntityException;
//	
//	QT getTemplate();


	public <
		XV extends Serializable, 
		XT extends ValueType<XT>, 
		XH extends ValueHolder<XV, XT, XH>, 
		K extends Attribute<A, E, ?, XV, XT, XH, K>
	> 
	EntityQueryValue value(K key);
	
	EntityQueryValue value(A name);

	
	<
		XV extends Serializable,
		XT extends ValueType<XT>,
		XH extends ValueHolder<XV, XT, XH>,	
		K extends Attribute<A, E, ?, XV, XT, XH, K>
	>
	EntityQueryPredicate newNull(K key);
	
	<
		XV extends Serializable,
		XT extends ValueType<XT>,
		XH extends ValueHolder<XV, XT, XH>,	
		K extends Attribute<A, E, ?, XV, XT, XH, K>
	>
	EntityQueryPredicate newNotNull(K key);
	
	<
		K extends Attribute<A, E, ?, ?, ?, ?, K>
	>
	EntityQueryPredicate newEquals(K key, EntityQueryValue value);
	
	<
		XV extends Serializable,
		XT extends ValueType<XT>,
		XH extends ValueHolder<XV, XT, XH>,	
		K extends Attribute<A, E, ?, XV, XT, XH, K>
	>
	EntityQueryPredicate newEquals(K key, XH value);
	
	<
		XV extends Serializable,
		XT extends ValueType<XT>,
		XH extends ValueHolder<XV, XT, XH>,	
		K extends Attribute<A, E, ?, XV, XT, XH, K>
	>
	EntityQueryPredicate newEquals(K key, XV value);
	
	<
		K extends Attribute<A, E, ?, ?, ?, ?, K>
	>
	EntityQueryPredicate newPredicate(K key, Comparison.Op op, EntityQueryValue value);
	
	<
		XV extends Serializable,
		XT extends ValueType<XT>,
		XH extends ValueHolder<XV, XT, XH>,	
		K extends Attribute<A, E, ?, XV, XT, XH, K>
	>
	EntityQueryPredicate newPredicate(K key, Comparison.Op op, XH value);

	<
		XV extends Serializable,
		XT extends ValueType<XT>,
		XH extends ValueHolder<XV, XT, XH>,	
		K extends Attribute<A, E, ?, XV, XT, XH, K>
	>
	EntityQuerySortKey newSortKey(K key, boolean ascending);


	
	public <	
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
	EntityQueryPredicate newEquals(K key, EntityReference<RA, RR, RT, RE, RB, RH, RF, RM> rhs);
	
	
	public <	
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
	EntityQueryPredicate newEquals(K key, Entity<RA, RR, RT, RE, RB, RH, RF, RM> rhs);	

	
	public <	
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
	EntityReference<RA, RR, RT, RE, RB, RH, RF, RM> ref(K key);
	
	

	
}
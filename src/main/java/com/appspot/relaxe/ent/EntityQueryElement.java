/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQueryValueReference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;

public interface EntityQueryElement<
	A extends com.appspot.relaxe.ent.Attribute,
	R extends com.appspot.relaxe.ent.Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,		
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends com.appspot.relaxe.ent.Content,	
	QE extends EntityQueryElement<A, R, T, E, H, F, M, C, QE>
>
	extends Request, EntityQueryElementTag
{	
//	DefaultTableExpression getTableExpression()
//		throws EntityException;
//	
	/**
	 * result item meta-data
	 * @return
	 */
	M getMetaData();
	
	// TableReference getTableRef();
	
	public int getElementCount();
		
	public Set<A> attributes();
		
	Collection<EntityQueryPredicate> predicates();
		
	public EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> getQueryElement(EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k);
	
	
	QE self();
	
	
	public interface Builder<
		A extends com.appspot.relaxe.ent.Attribute,
		R extends com.appspot.relaxe.ent.Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content,	
		QE extends EntityQueryElement<A, R, T, E, H, F, M, C, QE>
	> {
		
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> getQueryElement(EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k);
		
		public 
		<			
			RA extends com.appspot.relaxe.ent.Attribute,
			RR extends com.appspot.relaxe.ent.Reference,	
			RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
			RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
			RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
			RF extends EntityFactory<RE, RH, RM, RF, RC>,
			RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
			RC extends com.appspot.relaxe.ent.Content,
			RQ extends EntityQueryElement<RA, RR, RT, RE, RH, RF, RM, RC, RQ>,
			K extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, K>	
		>
		void setQueryElement(K key, RQ queryElement);
						
		void addAllAttributes();
		
		void add(A attribute);		
		void remove(A attribute);
		
		void add(com.appspot.relaxe.ent.value.PrimitiveKey<A, E, ?, ?, ?, ?> key);
		void remove(com.appspot.relaxe.ent.value.PrimitiveKey<A, E, ?, ?, ?, ?> key);
		
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
	
	<
		XV extends Serializable,
		XT extends PrimitiveType<XT>,
		XH extends PrimitiveHolder<XV, XT, XH>,	
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	>
	EntityQueryPredicate newNull(K key);
	
	<
		XV extends Serializable,
		XT extends PrimitiveType<XT>,
		XH extends PrimitiveHolder<XV, XT, XH>,	
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	>
	EntityQueryPredicate newNotNull(K key);
	
	<
		K extends PrimitiveKey<A, E, ?, ?, ?, K>
	>
	EntityQueryPredicate newEquals(K key, EntityQueryValueReference value);
	
	<
		XV extends Serializable,
		XT extends PrimitiveType<XT>,
		XH extends PrimitiveHolder<XV, XT, XH>,	
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	>
	EntityQueryPredicate newEquals(K key, XH value);
	
	<
		XV extends Serializable,
		XT extends PrimitiveType<XT>,
		XH extends PrimitiveHolder<XV, XT, XH>,	
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	>
	EntityQueryPredicate newEquals(K key, XV value);
	
	<
		K extends PrimitiveKey<A, E, ?, ?, ?, K>
	>
	EntityQueryPredicate newPredicate(K key, Comparison.Op op, EntityQueryValueReference value);
	
	<
		XV extends Serializable,
		XT extends PrimitiveType<XT>,
		XH extends PrimitiveHolder<XV, XT, XH>,	
		K extends PrimitiveKey<A, E, XV, XT, XH, K>
	>
	EntityQueryPredicate newPredicate(K key, Comparison.Op op, XH value);
	
}
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
import java.util.List;

import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQuerySortKey;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.ent.value.StringAttribute;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;

/**
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 *
 * @param <A>
 * @param <R>
 * @param <T>
 * @param <E>
 * @param <H>
 * @param <F>
 * @param <M>
 * @param <C>
 * @param <RT> Type of the template of the root element of the query
 * @param <RE> Type of the root element of the query
 */
public interface EntityQuery<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>
>
	extends Request {
	
	RE getRootElement();
	
	
	Collection<EntityQueryPredicate> predicates();	
	List<EntityQuerySortKey> sortKeys();
		
	interface Builder<
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,
		M extends EntityMetaData<A, R, T, E, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>
	> {
		void setRootElement(RE root);		
		RE getRootElement();
		
		void addPredicate(Identifier column, Comparison.Op op, ValueExpression ve);
		void addPredicate(EntityQueryPredicate p);
		
		public		
		<
			V extends Serializable,
			P extends ValueType<P>,
			W extends ValueHolder<V, P, W>,
			K extends Attribute<A, E, V, P, W, K>
		>
		void addPredicate(K key, Comparison.Op op, W value);
		
		public
		<
			V extends Serializable,
			P extends ValueType<P>,
			W extends ValueHolder<V, P, W>,
			K extends Attribute<A, E, V, P, W, K>
		>
		void addPredicate(K key, W holder);
		
		public <
			P extends ValueType<P>,
			W extends ValueHolder<String, P, W>,
			K extends StringAttribute<A, E, P, W, K>
		>
		void addPredicate(K key, String value);
		
		public <
			P extends ValueType<P>,
			W extends ValueHolder<String, P, W>,
			K extends StringAttribute<A, E, P, W, K>
		>
		void addPredicate(K key, Comparison.Op op, String value);			
		
		
				
		void addSortKey(EntityQuerySortKey key);
		
		public 
		<
			XA extends com.appspot.relaxe.ent.AttributeName,
			XE extends Entity<XA, ?, ?, XE, ?, ?, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, XQ>
		>		
		void asc(EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, XQ> element, Attribute<XA, XE, ?, ?, ?, ?> attribute);
				
		public 
		<
			XA extends com.appspot.relaxe.ent.AttributeName,
			XE extends Entity<XA, ?, ?, XE, ?, ?, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, XQ>
		>		
		void desc(EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, XQ> element, Attribute<XA, XE, ?, ?, ?, ?> attribute);
		
		
		public 
		<
			XA extends com.appspot.relaxe.ent.AttributeName,			
			XQ extends EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, XQ>
		>		
		void asc(EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, XQ> element, XA attribute);

		public 
		<
			XA extends com.appspot.relaxe.ent.AttributeName,			
			XQ extends EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, XQ>
		>		
		void desc(EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, XQ> element, XA attribute);
		
		void asc(Attribute<A, E, ?, ?, ?, ?> key);
		
		void desc(Attribute<A, E, ?, ?, ?, ?> key);
		
		
		EntityQuery<A, R, T, E, H, F, M, RE> newQuery();
	}
}

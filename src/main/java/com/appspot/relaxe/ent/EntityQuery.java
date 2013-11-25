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
import java.util.List;

import com.appspot.relaxe.ent.query.EntityQueryPredicate;
import com.appspot.relaxe.ent.query.EntityQuerySortKey;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.ent.value.StringKey;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;

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
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,	
	RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
>
	extends Request {
	
	RE getRootElement();
	
	
	Collection<EntityQueryPredicate> predicates();	
	List<EntityQuerySortKey> sortKeys();
	
//	/**
//	 * Returns the table reference the value in the <code>column</code> originated from. 
//	 * 
//	 * @param column
//	 * @return
//	 */
//	TableReference getOrigin(int column);
	
	interface Builder<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content,	
		RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
	> {
		void setRootElement(RE root);		
		RE getRootElement();
		
		void addPredicate(Identifier column, Comparison.Op op, ValueExpression ve);
		void addPredicate(EntityQueryPredicate p);
		
		public		
		<
			V extends Serializable,
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<V, P, W>,
			K extends PrimitiveKey<A, E, V, P, W, K>
		>
		void addPredicate(K key, Comparison.Op op, W value);
		
		public
		<
			V extends Serializable,
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<V, P, W>,
			K extends PrimitiveKey<A, E, V, P, W, K>
		>
		void addPredicate(K key, W holder);
		
		public <
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<String, P, W>,
			K extends StringKey<A, E, P, W, K>
		>
		void addPredicate(K key, String value);
		
		public <
			P extends PrimitiveType<P>,
			W extends PrimitiveHolder<String, P, W>,
			K extends StringKey<A, E, P, W, K>
		>
		void addPredicate(K key, Comparison.Op op, String value);			
		
		
				
		void addSortKey(EntityQuerySortKey key);
		
		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,
			XE extends Entity<XA, ?, ?, XE, ?, ?, ?, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ>
		>		
		void asc(EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ> element, PrimitiveKey<XA, XE, ?, ?, ?, ?> attribute);
				
		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,
			XE extends Entity<XA, ?, ?, XE, ?, ?, ?, ?>,
			XQ extends EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ>
		>		
		void desc(EntityQueryElement<XA, ?, ?, XE, ?, ?, ?, ?, XQ> element, PrimitiveKey<XA, XE, ?, ?, ?, ?> attribute);
		
		
		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,			
			XQ extends EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ>
		>		
		void asc(EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ> element, XA attribute);

		public 
		<
			XA extends com.appspot.relaxe.ent.Attribute,			
			XQ extends EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ>
		>		
		void desc(EntityQueryElement<XA, ?, ?, ?, ?, ?, ?, ?, XQ> element, XA attribute);
		
		void asc(PrimitiveKey<A, E, ?, ?, ?, ?> key);
		
		void desc(PrimitiveKey<A, E, ?, ?, ?, ?> key);
		
		
		EntityQuery<A, R, T, E, H, F, M, C, RE> newQuery();
	}
}

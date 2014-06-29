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
import java.util.Set;

import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;


/**
 * @param <A>
 * @param <R>
 * @param <Q>
 * @param <E>
 */


public abstract class DefaultEntity<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>
	extends AbstractEntity<A, R, T, E, B, H, F, M> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3498823449580706161L;

	protected DefaultEntity() {
		super();
	}


	@Override
	public <
		S extends Serializable, 
		P extends com.appspot.relaxe.types.ValueType<P>, 
		PH extends com.appspot.relaxe.value.ValueHolder<S, P, PH>, 
		K extends com.appspot.relaxe.ent.value.Attribute<A, E, B, S, P, PH, K>
	> 
	PH get(K k) {
		return k.get(self());
	}
	
		
	@Override
	public <
		RA extends AttributeName,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		RK extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK>
	> RH getRef(EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK> k) {
		return k.get(self());
	}
	
//	@Override
//	public <
//		RA extends AttributeName,
//		RR extends Reference,
//		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
//		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
//		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
//		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
//		RF extends EntityFactory<RE, RB, RH, RM, RF>,
//		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
//		RK extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK>
//	> 
//	void setRef(EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK> k, RH newValue) {		
//		k.set(self(), newValue);
//	}
	
	@Override
	public Entity<?, ?, ?, ?, ?, ?, ?, ?> getRef(R r) {
		EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(r);
		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return (rh == null) ? null : rh.value();		
	}
	
	@Override
	public abstract Set<A> attributes();
	
	
	@Override
	public com.appspot.relaxe.value.ReferenceHolder<?,?,?,?,?,?> ref(R ref) {
		EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(ref);
		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return rh;
	}	
}

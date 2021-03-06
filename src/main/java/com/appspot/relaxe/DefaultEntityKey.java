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
package com.appspot.relaxe;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public abstract class DefaultEntityKey<
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
	K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
>
	implements EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4023544205660547860L;
	private M source;
	private RM destination;		
	private R name;
	
	protected DefaultEntityKey() {
	}
		
	public DefaultEntityKey(M source, RM destination, R name) {
		super();
		this.source = source;
		this.destination = destination;
		this.name = name;
		
		if (destination.type() != name.type()) {
			throw new IllegalArgumentException("type mismatch: reference-type="  +name.type() + "; destination-type=" + destination.type()); 
		} 
	}
	
	@Override
	public M getSource() {
		return this.source;
	}
	
	
	@Override
	public RM getTarget() {
		return destination;
	}

	@Override
	public R name() {
		return this.name;
	}

	@Override
	public abstract RH newHolder(RE newValue);

	@Override
	public abstract void set(B e, RH newValue);

	@Override
	public void set(B e, RE newValue) {		
		e.setRef(self(), newHolder(newValue));
	}

	@Override
	public RE value(E e) {
		RH h = get(e);
		return (h == null) ? null : h.value();
	}
	
	@Override
	public RH get(E e) {
		RH ref = e.getRef(self());
		return ref;
	};

			
	@Override
	public RT type() {	
		return destination.type();
	}
	
	@Override
	public abstract K self();

	
	
	
	@Override
	public void copy(E src, B dest) {
		RH v = src.getRef(self());
		dest.setRef(self(), v);		
	}

}

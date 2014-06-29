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
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public abstract class AbstractEntityKey<	
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
	private static final long serialVersionUID = -8135907988547652994L;
	/**
	 *
	 */
	private R name;
	private T source;
		
//	protected AbstractEntityKey() {		
//	}	
	
	@Override
	public abstract RH get(E e);
	
	@Override
	public abstract void set(B e, RH newValue);
	
//	@Override
//	public void set(E e, H newValue) {		
//		e.setRef(self(), newValue);
//	}
		
	@Override
	public void set(B e, RE newValue) {
		e.setRef(self(), newHolder(newValue));
	}

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractEntityKey() {
	}
	
	protected AbstractEntityKey(T source, R name) {
		setName(name);
		
		if (source == null) {
			throw new NullPointerException("source");
		}
		
		this.source = source;
	}

	private void setName(R name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;
	}

	@Override
	public R name() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode() ^ this.name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			throw new NullPointerException("o");
		}
		
		if (o == this) {
			return true;
		}
		
		if(!getClass().equals(o.getClass())) {
			return false;
		}
		
		// Since getClass().equals(o.getClass()) implies t.type().getSqlType() == type().getSqlType()
		// we only need to check the name:		
		AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = 
				(AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;								
		return nameEquals(t);
	}
	
	private boolean nameEquals(AbstractEntityKey<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> pk) {		
		return name().equals(pk.name());
	}
//	
//	@Override
//	public H get(E e) {				
//		H rh = e.getRef(self());
//		return rh;
//	}
//	
//	public void set(E e, H newValue) {
//		e.setRef(self(), newValue);		
//	}
	
	@Override
	public RE value(E e) {
		RH h = get(e);
		return (h == null) ? null : h.value();		
	}
	
	@Override
	public abstract RT type();
	
	@Override
	public void copy(E src, B dest) {
		K k = self();
		dest.setRef(k, src.getRef(k));
	};
	
	@Override
	public void reset(B dest) {
		dest.setRef(self(), newHolder(null));
	}

	
	@Override
	public M getSource() {
		return source.getMetaData();
	}
	
	
	
}

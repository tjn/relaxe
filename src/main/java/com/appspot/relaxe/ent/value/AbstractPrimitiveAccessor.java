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

import java.io.Serializable;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public abstract class AbstractPrimitiveAccessor<
	A extends AttributeName,
	E,
	S extends Serializable,
	P extends AbstractPrimitiveType<P>,
	H extends AbstractPrimitiveHolder<S, P, H>,	
	K extends ValueAttribute<A, E, S, P, H, K>
	>
	implements PrimitiveAccessor<A, E, S, P, H, K>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7060596100410117898L;
	
	private E target;
	private K key;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractPrimitiveAccessor() {
	}

	public AbstractPrimitiveAccessor(E target, K key) {
		super();

		if (target == null) {
			throw new NullPointerException("target");
		}		
		
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		this.target = target;
		this.key = key;
	}

	public A attribute() {
		return key().name();
	}

	@Override
	public K key() {
		return this.key;
	}

//	public abstract void setHolder(H newHolder);
	
	public void setHolder(H newHolder) {
		key().set(getTarget(), newHolder);
	}

	@Override
	public H getHolder() 
		throws EntityRuntimeException {		
		return this.key().get(getTarget());				
	}

	public void set(S newValue) 
		throws EntityRuntimeException {
		setHolder(this.key.newHolder(newValue));
	}
		
	@Override
	public S get() 
		throws EntityRuntimeException {
		H h = getHolder();
		return (h == null) ? null : h.value();
	}

	public E getTarget() {
		return target;
	}

	protected void setTarget(E target) {
		if (target == null) {
			throw new NullPointerException("target");
		}
		
		this.target = target;
	}
}

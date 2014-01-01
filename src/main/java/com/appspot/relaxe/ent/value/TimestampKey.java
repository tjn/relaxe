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

import java.util.Date;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.TimestampHolder;
import com.appspot.relaxe.types.TimestampType;


public final class TimestampKey<	
	A extends Attribute,
	E extends HasTimestamp<A, E>
>
	extends AbstractPrimitiveKey<A, E, Date, TimestampType, TimestampHolder, TimestampKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7215861304511882107L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimestampKey() {
	}

	private TimestampKey(HasTimestampKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasTimestamp<X, T>
	>
	TimestampKey<X, T> get(HasTimestampKey<X, T> meta, X a) {
		TimestampKey<X, T> k = meta.getTimestampKey(a);
		
		if (k == null) {						
			if (TimestampType.TYPE.equals(a.type())) {
				k = new TimestampKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public TimestampType type() {
		return TimestampType.TYPE;
	}

	@Override
	public void set(E e, TimestampHolder newValue) 
		throws EntityRuntimeException {
		e.setTimestamp(this, newValue);
	}
	
	@Override
	public TimestampHolder get(E e) 
		throws EntityRuntimeException {
		return e.getTimestamp(this);
	}
	
	@Override
	public TimestampHolder newHolder(Date newValue) {
		return TimestampHolder.valueOf(newValue);
	}
	
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {		
		dest.setTimestamp(this, src.getTimestamp(this));		
	}

	@Override
	public TimestampKey<A, E> self() {
		return this;
	}

	@Override
	public TimestampHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return TimestampHolder.of(holder);
	}
}

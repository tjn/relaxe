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
package com.appspot.relaxe.ent.value;

import java.util.Date;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.TimeHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.TimeType;


public final class TimeKey<
	A extends Attribute,
	E extends HasTime<A, E>
>
	extends AbstractPrimitiveKey<A, E, Date, TimeType, TimeHolder, TimeKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7215861304511882107L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimeKey() {
	}

	private TimeKey(HasTimeKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasTime<X, T>
	>
	TimeKey<X, T> get(HasTimeKey<X, T> meta, X a) {
		TimeKey<X, T> k = meta.getTimeKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (TimeType.TYPE.equals(t)) {
				k = new TimeKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public TimeType type() {
		return TimeType.TYPE;
	}

	@Override
	public void set(E e, TimeHolder newValue) {
		e.setTime(this, newValue);
	}
	
	@Override
	public TimeHolder get(E e) {
		return e.getTime(this);
	}
	
	@Override
	public TimeHolder newHolder(Date newValue) {
		return TimeHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setTime(this, src.getTime(this));		
	}

	@Override
	public TimeKey<A, E> self() {
		return this;
	}

	@Override
	public TimeHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return TimeHolder.of(holder);
	}

}

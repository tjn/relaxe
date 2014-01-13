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

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.TimeType;
import com.appspot.relaxe.value.TimeHolder;
import com.appspot.relaxe.value.ValueHolder;


public final class TimeAttribute<
	A extends AttributeName,
	E extends HasTime<A, E>
>
	extends AbstractValueAttribute<A, E, Date, TimeType, TimeHolder, TimeAttribute<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7215861304511882107L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimeAttribute() {
	}

	private TimeAttribute(HasTimeAttribute<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,
		T extends HasTime<X, T>
	>
	TimeAttribute<X, T> get(HasTimeAttribute<X, T> meta, X a) {
		TimeAttribute<X, T> k = meta.getTimeAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (TimeType.TYPE.equals(t)) {
				k = new TimeAttribute<X, T>(meta, a);
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
	public TimeAttribute<A, E> self() {
		return this;
	}

	@Override
	public TimeHolder as(ValueHolder<?, ?, ?> holder) {
		return TimeHolder.of(holder);
	}

}

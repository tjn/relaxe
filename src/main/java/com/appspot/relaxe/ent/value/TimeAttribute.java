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
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.types.TimeType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.TimeHolder;
import com.appspot.relaxe.value.ValueHolder;


public final class TimeAttribute<
	A extends AttributeName,
	E extends HasTime.Read<A, E, B>,
	B extends HasTime.Write<A, E, B>
>
	extends AbstractAttribute<A, E, B, Date, TimeType, TimeHolder, TimeAttribute<A, E, B>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimeAttribute() {
	}
	
	private TimeAttribute(A name) {
		super(name);		
	}
	
	public static <
		X extends AttributeName,
		T extends HasTime.Read<X, T, S>,
		S extends HasTime.Write<X, T, S>
	>
	TimeAttribute<X, T, S> get(HasTimeAttribute<X, T, S> meta, X a) {
		TimeAttribute<X, T, S> k = meta.getTimeAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (t != null && TimeType.TYPE.equals(t)) {
				k = new TimeAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
		
	@Override
	public TimeType type() {
		return TimeType.TYPE;
	}
	
	@Override
	public void set(B e, TimeHolder newValue) {
		e.setTime(this, newValue);
	}
	
	@Override
	public TimeHolder get(E e) {
		return e.getTime(self());
	}
	
	@Override
	public TimeHolder newHolder(Date newValue) {
		return TimeHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, B dest) {
		dest.setTime(this, src.getTime(this));
	}
	
	@Override
	public TimeAttribute<A, E, B> self() {
		return this;
	}
	
	@Override
	public void reset(B dest) throws EntityRuntimeException {
		dest.setTime(this, TimeHolder.NULL_HOLDER);
	}
		
	@Override
	public TimeHolder as(ValueHolder<?, ?, ?> unknown) {
		return unknown.asTimeHolder();
	}
}

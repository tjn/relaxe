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
import com.appspot.relaxe.types.TimestampType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.TimestampHolder;
import com.appspot.relaxe.value.ValueHolder;


public final class TimestampAttribute<
	A extends AttributeName,
	E extends HasTimestamp.Read<A, E, B>,
	B extends HasTimestamp.Write<A, E, B>
	>
	extends AbstractAttribute<A, E, B, Date, TimestampType, TimestampHolder, TimestampAttribute<A, E, B>>	
	{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private TimestampAttribute() {
	}
	
	private TimestampAttribute(A name) {
		super(name);		
	}
	
	public static <
		X extends AttributeName,
		T extends HasTimestamp.Read<X, T, S>,
		S extends HasTimestamp.Write<X, T, S>
	>
	TimestampAttribute<X, T, S> get(HasTimestampAttribute<X, T, S> meta, X a) {
		TimestampAttribute<X, T, S> k = meta.getTimestampAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (t != null && TimestampType.TYPE.equals(t)) {
				k = new TimestampAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
		
	@Override
	public TimestampType type() {
		return TimestampType.TYPE;
	}
	
	@Override
	public void set(B e, TimestampHolder newValue) {
		e.setTimestamp(this, newValue);
	}
	
	@Override
	public TimestampHolder get(E e) {
		return e.getTimestamp(self());
	}
	
	@Override
	public TimestampHolder newHolder(Date newValue) {
		return TimestampHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, B dest) {
		dest.setTimestamp(this, src.getTimestamp(this));
	}
	
	@Override
	public TimestampAttribute<A, E, B> self() {
		return this;
	}
	
	@Override
	public void reset(B dest) throws EntityRuntimeException {
		dest.setTimestamp(this, TimestampHolder.NULL_HOLDER);
	}
		
	@Override
	public TimestampHolder as(ValueHolder<?, ?, ?> unknown) {
		return unknown.asTimestampHolder();
	}
}

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
import com.appspot.relaxe.types.DateType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.DateHolder;
import com.appspot.relaxe.value.ValueHolder;


public final class DateAttribute<
	A extends AttributeName,
	E extends HasDate.Read<A, E, B>,
	B extends HasDate.Write<A, E, B>
>
	extends AbstractAttribute<A, E, B, Date, DateType, DateHolder, DateAttribute<A, E, B>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DateAttribute() {
	}
	
	private DateAttribute(A name) {
		super(name);		
	}
	
	public static <
		X extends AttributeName,
		T extends HasDate.Read<X, T, S>,
		S extends HasDate.Write<X, T, S>
	>
	DateAttribute<X, T, S> get(HasDateAttribute<X, T, S> meta, X a) {
		DateAttribute<X, T, S> k = meta.getDateAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (t != null && DateType.TYPE.equals(t)) {
				k = new DateAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
		
	@Override
	public DateType type() {
		return DateType.TYPE;
	}
	
	@Override
	public void set(B e, DateHolder newValue) {
		e.setDate(this, newValue);
	}
	
	@Override
	public DateHolder get(E e) {
		return e.getDate(self());
	}
	
	@Override
	public DateHolder newHolder(Date newValue) {
		return DateHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, B dest) {
		dest.setDate(this, src.getDate(this));
	}
	
	@Override
	public DateAttribute<A, E, B> self() {
		return this;
	}
	
	@Override
	public void reset(B dest) throws EntityRuntimeException {
		dest.setDate(this, DateHolder.NULL_HOLDER);
	}
		
	@Override
	public DateHolder as(ValueHolder<?, ?, ?> unknown) {
		return unknown.asDateHolder();
	}
}

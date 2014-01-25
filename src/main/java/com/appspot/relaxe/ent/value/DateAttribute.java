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
	E extends HasDate<A, E>
>
	extends AbstractAttribute<A, E, Date, DateType, DateHolder, DateAttribute<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = -8003688473297829554L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DateAttribute() {
	}

	private DateAttribute(HasDateAttribute<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,
		T extends HasDate<X, T>
	>
	DateAttribute<X, T> get(HasDateAttribute<X, T> meta, X a) {
		DateAttribute<X, T> k = meta.getDateAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (t != null && DateType.TYPE.equals(t)) {
				k = new DateAttribute<X, T>(meta, a);
			}
		}
				
		return k;
	}

	
	@Override
	public DateType type() {
		return DateType.TYPE;
	}

	@Override
	public void set(E e, DateHolder newValue) 
		throws EntityRuntimeException {
		e.setDate(this, newValue);
	}
	
	@Override
	public DateHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDate(this);
	}
	
	@Override
	public DateHolder newHolder(Date newValue) {
		return DateHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, E dest)
		throws EntityRuntimeException {
		dest.setDate(this, src.getDate(this));
	}

	@Override
	public DateAttribute<A, E> self() {
		return this;		
	}
	
	@Override
	public DateHolder as(ValueHolder<?, ?, ?> holder) {
		return DateHolder.as(holder);
	}
}

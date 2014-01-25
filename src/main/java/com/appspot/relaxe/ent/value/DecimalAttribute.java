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
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.types.DecimalType;
import com.appspot.relaxe.value.Decimal;
import com.appspot.relaxe.value.DecimalHolder;
import com.appspot.relaxe.value.ValueHolder;

public final class DecimalAttribute<
	A extends AttributeName,
	E extends HasDecimal<A, E>
>
	extends AbstractAttribute<A, E, Decimal, DecimalType, DecimalHolder, DecimalAttribute<A, E>>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1332513199153161810L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DecimalAttribute() {
	}

	private DecimalAttribute(HasDecimalAttribute<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,		
		T extends HasDecimal<X, T>
	>
	DecimalAttribute<X, T> get(HasDecimalAttribute<X, T> meta, X a) {
		DecimalAttribute<X, T> k = meta.getDecimalAttribute(a);
		
		if (k == null) {
			if (DecimalType.TYPE.equals(a.type())) {
				k = new DecimalAttribute<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DecimalType type() {
		return DecimalType.TYPE;
	}

	@Override
	public void set(E e, DecimalHolder newValue)
		throws EntityRuntimeException {
		e.setDecimal(this, newValue);
	}
	
	@Override
	public DecimalHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDecimal(this);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDecimal(this, src.getDecimal(this));
	}

	@Override
	public DecimalHolder newHolder(Decimal newValue) {
		return DecimalHolder.valueOf(newValue);
	}
	
	@Override
	public DecimalAttribute<A, E> self() {
		return this;
	}
	
	@Override
	public DecimalHolder as(ValueHolder<?, ?, ?> holder) {
		return DecimalHolder.of(holder);
	}
}

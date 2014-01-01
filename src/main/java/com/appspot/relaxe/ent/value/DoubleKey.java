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

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.DoubleHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.DoubleType;
import com.appspot.relaxe.types.PrimitiveType;

public final class DoubleKey<	
	A extends Attribute,	
	E extends HasDouble<A, E>
>
	extends AbstractPrimitiveKey<A, E, Double, DoubleType, DoubleHolder, DoubleKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1065150474303051699L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DoubleKey() {
	}

	private DoubleKey(HasDoubleKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasDouble<X, T>
	>
	DoubleKey<X, T> get(HasDoubleKey<X, T> meta, X a) {
		DoubleKey<X, T> k = meta.getDoubleKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (DoubleType.TYPE.equals(t)) {
				k = new DoubleKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DoubleType type() {
		return DoubleType.TYPE;
	}

	@Override
	public void set(E e, DoubleHolder newValue) 
		throws EntityRuntimeException {
		e.setDouble(this, newValue);
	}
	
	@Override
	public DoubleHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDouble(this);
	}

	@Override
	public DoubleHolder newHolder(Double newValue) {
		return DoubleHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDouble(this, src.getDouble(this));
	}
	
	@Override
	public DoubleKey<A, E> self() {
		return this;
	}
	
	@Override
	public DoubleHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return DoubleHolder.of(holder);
	}
}

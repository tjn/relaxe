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
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.ValueHolder;

public final class IntegerAttribute<
	A extends AttributeName,
	E extends HasInteger.Read<A, E, B>,
	B extends HasInteger.Write<A, E, B>
>
	extends AbstractAttribute<A, E, B, Integer, IntegerType, IntegerHolder, IntegerAttribute<A, E, B>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private IntegerAttribute() {
	}

	private IntegerAttribute(A name) {
		super(name);		
	}
	
	public static <
		X extends AttributeName,
		T extends HasInteger.Read<X, T, S>,
		S extends HasInteger.Write<X, T, S>
	>
	IntegerAttribute<X, T, S> get(HasIntegerAttribute<X, T, S> meta, X a) {
		IntegerAttribute<X, T, S> k = meta.getIntegerAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (t != null && IntegerType.TYPE.equals(t)) {
				k = new IntegerAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
		
	@Override
	public IntegerType type() {
		return IntegerType.TYPE;
	}
	
	@Override
	public void set(B e, IntegerHolder newValue) {
		e.setInteger(this, newValue);
	}
	
	@Override
	public IntegerHolder get(E e) {
		return e.getInteger(self());
	}
	
	@Override
	public IntegerHolder newHolder(Integer newValue) {
		return IntegerHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, B dest) {
		dest.setInteger(this, src.getInteger(this));
	}

	@Override
	public IntegerAttribute<A, E, B> self() {
		return this;
	}

	@Override
	public void reset(B dest) throws EntityRuntimeException {
		dest.setInteger(this, IntegerHolder.NULL_HOLDER);
	}
		
	@Override
	public IntegerHolder as(ValueHolder<?, ?, ?> unknown) {
		return unknown.asIntegerHolder();
	}
}

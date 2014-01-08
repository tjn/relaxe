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
import com.appspot.relaxe.rpc.BooleanHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.BooleanType;
import com.appspot.relaxe.types.PrimitiveType;

public final class BooleanKey<
	A extends AttributeName,
	E extends HasBoolean<A, E>
>
	extends AbstractPrimitiveKey<A, E, Boolean, BooleanType, BooleanHolder, BooleanKey<A, E>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private BooleanKey() {
	}

	private BooleanKey(HasBooleanKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,
		T extends HasBoolean<X, T>
	>
	BooleanKey<X, T> get(HasBooleanKey<X, T> meta, X a) {
		BooleanKey<X, T> k = meta.getBooleanKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && BooleanType.TYPE.equals(t)) {
				k = new BooleanKey<X, T>(meta, a);
			}
		}
				
		return k;
	}
		
	@Override
	public BooleanType type() {
		return BooleanType.TYPE;
	}
	
	@Override
	public void set(E e, BooleanHolder newValue) {
		e.setBoolean(this, newValue);
	}
	
	@Override
	public BooleanHolder get(E e) {
		return e.getBoolean(self());
	}
	
	@Override
	public BooleanHolder newHolder(Boolean newValue) {
		return BooleanHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setBoolean(this, src.getBoolean(this));
	}

	@Override
	public BooleanKey<A, E> self() {
		return this;
	}

	@Override
	public void reset(E dest) throws EntityRuntimeException {
		dest.setBoolean(this, BooleanHolder.NULL_HOLDER);
	}
		
	@Override
	public BooleanHolder as(PrimitiveHolder<?, ?, ?> unknown) {
		return unknown.asBooleanHolder();
	}
}

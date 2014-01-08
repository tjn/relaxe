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
import com.appspot.relaxe.rpc.LongVarBinaryHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.LongVarBinaryType;
import com.appspot.relaxe.types.PrimitiveType;

public final class LongVarBinaryKey<
	A extends AttributeName,
	E extends HasLongVarBinary<A, E>
>
	extends AbstractPrimitiveKey<A, E, LongVarBinary, LongVarBinaryType, LongVarBinaryHolder, LongVarBinaryKey<A, E>>	
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9081341365015015217L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private LongVarBinaryKey() {
	}

	private LongVarBinaryKey(HasLongVarBinaryKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,
		T extends HasLongVarBinary<X, T>
	>
	LongVarBinaryKey<X, T> get(HasLongVarBinaryKey<X, T> meta, X a) {
		LongVarBinaryKey<X, T> k = meta.getLongVarBinaryKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && LongVarBinaryType.TYPE.equals(t)) {
				k = new LongVarBinaryKey<X, T>(meta, a);
			}
		}
				
		return k;
	}
		
	@Override
	public LongVarBinaryType type() {
		return LongVarBinaryType.TYPE;
	}
	
	@Override
	public void set(E e, LongVarBinaryHolder newValue) {
		e.setLongVarBinary(this, newValue);
	}
	
	@Override
	public LongVarBinaryHolder get(E e) {
		return e.getLongVarBinary(self());
	}
	
	@Override
	public LongVarBinaryHolder newHolder(LongVarBinary newValue) {
		return LongVarBinaryHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setLongVarBinary(this, src.getLongVarBinary(this));
	}

	@Override
	public LongVarBinaryKey<A, E> self() {
		return this;
	}

	@Override
	public void reset(E dest) throws EntityRuntimeException {
		dest.setLongVarBinary(this, LongVarBinaryHolder.NULL_HOLDER);
	}
		
	@Override
	public LongVarBinaryHolder as(PrimitiveHolder<?, ?, ?> unknown) {
		return unknown.asLongVarBinaryHolder();
	}
}

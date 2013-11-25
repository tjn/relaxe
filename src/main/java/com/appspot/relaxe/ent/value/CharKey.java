/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
import com.appspot.relaxe.rpc.CharHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.CharType;

public final class CharKey<
	A extends Attribute,	
	E extends HasChar<A, E> & HasString<A, E>
>
	extends StringKey<A, E, CharType, CharHolder, CharKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1117929153888182121L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private CharKey() {
	}
	
	private CharKey(HasCharKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}	
	
	public static <
		X extends Attribute,
		T extends HasChar<X, T> & HasString<X, T>
	>
	CharKey<X, T> get(HasCharKey<X, T> meta, X a) {
		CharKey<X, T> k = meta.getCharKey(a);
		
		if (k == null) {
			if (CharType.TYPE.equals(a.type())) {
				k = new CharKey<X, T>(meta, a);
			}
		}
				
		return k;
	}


	@Override
	public CharType type() {
		return CharType.TYPE;
	}
	
	@Override
	public void set(E e, CharHolder newValue)
		throws EntityRuntimeException {
		e.setChar(this, newValue);
	}
	
	@Override
	public CharHolder get(E e)
		throws EntityRuntimeException {
		return e.getChar(this);
	}

	@Override
	public CharHolder newHolder(String newValue) {
		return CharHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest)
		throws EntityRuntimeException {
		dest.setChar(this, src.getChar(this));
	}
	
	@Override
	public CharKey<A, E> self() {	
		return this;
	}
	
	@Override
	public CharHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return CharHolder.of(holder);
	}
}

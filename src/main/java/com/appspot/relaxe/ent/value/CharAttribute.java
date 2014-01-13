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
import com.appspot.relaxe.types.CharType;
import com.appspot.relaxe.value.CharHolder;
import com.appspot.relaxe.value.ValueHolder;

public final class CharAttribute<
	A extends AttributeName,	
	E extends HasChar<A, E> & HasString<A, E>
>
	extends StringAttribute<A, E, CharType, CharHolder, CharAttribute<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1117929153888182121L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private CharAttribute() {
	}
	
	private CharAttribute(HasCharAttribute<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}	
	
	public static <
		X extends AttributeName,
		T extends HasChar<X, T> & HasString<X, T>
	>
	CharAttribute<X, T> get(HasCharAttribute<X, T> meta, X a) {
		CharAttribute<X, T> k = meta.getCharAttribute(a);
		
		if (k == null) {
			if (CharType.TYPE.equals(a.type())) {
				k = new CharAttribute<X, T>(meta, a);
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
	public CharAttribute<A, E> self() {	
		return this;
	}
	
	@Override
	public CharHolder as(ValueHolder<?, ?, ?> holder) {
		return CharHolder.of(holder);
	}
}

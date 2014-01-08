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
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.value.HasString;
import com.appspot.relaxe.ent.value.StringKey;
import com.appspot.relaxe.rpc.PrimitiveHolder;


public class PGTextKey<
	A extends AttributeName,
	E extends HasPGText<A, E> & HasString<A, E>	
>
	extends StringKey<A, E, PGTextType, PGTextHolder, PGTextKey<A, E>>
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private PGTextKey() {
	}
	
	private PGTextKey(HasPGTextKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,
		T extends HasPGText<X, T> & HasString<X, T>
	>
	PGTextKey<X, T> get(HasPGTextKey<X, T> meta, X a) {
		PGTextKey<X, T> k = meta.getPGTextKey(a);
		
		if (k == null) {						
			if (PGTextType.TYPE.equals(a.type())) {
				k = new PGTextKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}


			
	@Override
	public PGTextType type() {
		return PGTextType.TYPE;
	}
	
	@Override
	public void set(E e, PGTextHolder newValue) 
		throws EntityRuntimeException {
		e.setPGText(this, newValue);
	}
	
	@Override
	public PGTextHolder get(E e) 
		throws EntityRuntimeException {
		return e.getPGText(this);
	}
	
		
//	@Override
//	public PGTextHolder newHolder(PGText newValue) {
//		return PGTextHolder.valueOf(newValue);
//	}

	@Override
	public void copy(E src, E dest) {
		dest.setPGText(this, src.getPGText(this));
	}

	@Override
	public PGTextKey<A, E> self() {
		return this;
	}

	@Override
	public PGTextHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return PGTextHolder.of(holder);
	}

	@Override
	public PGTextHolder newHolder(String newValue) {
		return PGTextHolder.valueOf(newValue);
	}
}

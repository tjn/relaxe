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
import com.appspot.relaxe.ent.value.StringAttribute;
import com.appspot.relaxe.value.ValueHolder;


public final class PGTextAttribute<
	A extends AttributeName,	
	E extends HasPGText.Read<A, E, W> & HasString.Read<A, E, W>,
	W extends HasPGText.Write<A, E, W> & HasString.Write<A, E, W>
>
	extends StringAttribute<A, E, W, PGTextType, PGTextHolder, PGTextAttribute<A, E, W>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1117929153888182121L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private PGTextAttribute() {
	}
	
	private PGTextAttribute(A name) {
		super(name);
		
	}	
	
	public static <
		X extends AttributeName,
		T extends HasPGText.Read<X, T, S> & HasString.Read<X, T, S>,
		S extends HasPGText.Write<X, T, S> & HasString.Write<X, T, S>
	>
	PGTextAttribute<X, T, S> get(HasPGTextAttribute<X, T, S> meta, X a) {
		PGTextAttribute<X, T, S> k = meta.getPGTextAttribute(a);
		
		if (k == null) {
			if (PGTextType.TYPE.equals(a.type())) {
				k = new PGTextAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
	
	
	@Override
	public PGTextType type() {
		return PGTextType.TYPE;
	}
	
	@Override
	public void set(W e, PGTextHolder newValue) {
		 e.setPGText(this, newValue);
	}
	
	@Override
	public PGTextHolder get(E e)
		throws EntityRuntimeException {
		return e.getPGText(this);
	}
	
	@Override
	public PGTextHolder newHolder(String newValue) {
		return PGTextHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, W dest) {
		dest.setPGText(this, src.getPGText(this));
	}
	
	@Override
	public PGTextAttribute<A, E, W> self() {	
		return this;
	}
	
	@Override
	public PGTextHolder as(ValueHolder<?, ?, ?> holder) {
		return PGTextHolder.of(holder);
	}
}

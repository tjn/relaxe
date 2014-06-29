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
import com.appspot.relaxe.ent.value.AbstractArrayAttribute;
import com.appspot.relaxe.value.StringArray;
import com.appspot.relaxe.value.ValueHolder;

public class PGTextArrayAttribute<
	A extends AttributeName,
	R extends HasPGTextArray.Read<A, R, W>,
	W extends HasPGTextArray.Write<A, R, W>
>
	extends AbstractArrayAttribute<A, R, W, String, StringArray, PGTextType, PGTextArrayType, PGTextArrayHolder, PGTextArrayAttribute<A, R, W>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected PGTextArrayAttribute() {
	}
		
	private PGTextArrayAttribute(A name) {
		super(name);
	}
	
	@Override
	public PGTextArrayAttribute<A, R, W> self() {
		return this;
	}
	
	@Override
	public PGTextArrayType type() {
		return PGTextArrayType.TYPE;
	}
		
	public static <
		X extends AttributeName,
		T extends HasPGTextArray.Read<X, T, S>,
		S extends HasPGTextArray.Write<X, T, S>
	>
	PGTextArrayAttribute<X, T, S> get(HasPGTextArrayAttribute<X, T, S> meta, X a) {
		PGTextArrayAttribute<X, T, S> k = meta.getPGTextArrayAttribute(a);
		
		if (k == null) {						
			if (PGTextArrayType.TYPE.equals(a.type())) {
				k = new PGTextArrayAttribute<X, T, S>(a);
				meta.register(k);
			}			
		}
				
		return k;
	}
	
	@Override
	public PGTextArrayHolder get(R e) throws EntityRuntimeException {
		return e.getPGTextArray(this);
	}
	
	@Override
	public void set(W e, PGTextArrayHolder newValue)
			throws EntityRuntimeException {
		e.setPGTextArray(this, newValue);
	}
	
	@Override
	public PGTextArrayHolder as(ValueHolder<?, ?, ?> holder) {
		return PGTextArrayHolder.of(holder);
	}

	@Override
	public PGTextArrayHolder newHolder(StringArray newValue) {
		return PGTextArrayHolder.valueOf(newValue);
	}
}


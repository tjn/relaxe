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
import com.appspot.relaxe.ent.value.AbstractArrayKey;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.VarcharType;


public class PGTextArrayKey<
	A extends AttributeName,
	E extends HasPGTextArray<A, E>	
>
	extends AbstractArrayKey<A, E, String, StringArray, VarcharType, PGTextArrayType, PGTextArrayHolder, PGTextArrayKey<A, E>>
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private PGTextArrayKey() {
	}
	
	private PGTextArrayKey(HasPGTextArrayKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,
		T extends HasPGTextArray<X, T>
	>
	PGTextArrayKey<X, T> get(HasPGTextArrayKey<X, T> meta, X a) {
		PGTextArrayKey<X, T> k = meta.getPGTextArrayKey(a);
		
		if (k == null) {
			ArrayType<?, ?> t = a.type().asArrayType();
			
			if (t != null && PGTextArrayType.TYPE.equals(a.type())) {
				k = new PGTextArrayKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}	

			
	@Override
	public PGTextArrayType type() {
		return PGTextArrayType.TYPE;
	}
	
	@Override
	public void set(E e, PGTextArrayHolder newValue) 
		throws EntityRuntimeException {
		e.setPGTextArray(this, newValue);
	}
	
	@Override
	public PGTextArrayHolder get(E e) 
		throws EntityRuntimeException {
		return e.getPGTextArray(this);
	}
	
		
//	@Override
//	public PGTextArrayHolder newHolder(PGTextArray newValue) {
//		return PGTextArrayHolder.valueOf(newValue);
//	}

	@Override
	public void copy(E src, E dest) {
		dest.setPGTextArray(this, src.getPGTextArray(this));
	}

	@Override
	public PGTextArrayKey<A, E> self() {
		return this;
	}

	@Override
	public PGTextArrayHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return PGTextArrayHolder.of(holder);
	}

	@Override
	public PGTextArrayHolder newHolder(StringArray newValue) {
		return PGTextArrayHolder.valueOf(newValue);
	}
}

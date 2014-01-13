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
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.types.VarcharType;

public final class VarcharAttribute<
	A extends AttributeName,
	E extends HasVarchar<A, E> & HasString<A, E>
>
	extends StringAttribute<A, E, VarcharType, VarcharHolder, VarcharAttribute<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private VarcharAttribute() {
	}

	private VarcharAttribute(HasVarcharAttribute<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends AttributeName,
		T extends HasVarchar<X, T> & HasString<X, T>
	>
	VarcharAttribute<X, T> get(HasVarcharAttribute<X, T> meta, X a) {
		VarcharAttribute<X, T> k = meta.getVarcharAttribute(a);
		
		if (k == null) {						
			if (VarcharType.TYPE.equals(a.type())) {
				k = new VarcharAttribute<X, T>(meta, a);
			}			
		}
				
		return k;
	}
	
	@Override
	public VarcharType type() {
		return VarcharType.TYPE;
	}
	
	@Override
	public void set(E e, VarcharHolder newValue) 
		throws EntityRuntimeException {
		e.setVarchar(this, newValue);
	}
	
	@Override
	public VarcharHolder get(E e) 
		throws EntityRuntimeException {
		return e.getVarchar(this);
	}
	
	@Override
	public VarcharHolder newHolder(String newValue) {
		return VarcharHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setVarchar(this, src.getVarchar(this));		
	}
	
	@Override
	public VarcharAttribute<A, E> self() {
		return this;
	}
	
	@Override
	public VarcharHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return VarcharHolder.of(holder);
	}
}

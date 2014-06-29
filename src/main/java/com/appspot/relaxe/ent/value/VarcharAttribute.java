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
import com.appspot.relaxe.types.VarcharType;
import com.appspot.relaxe.value.ValueHolder;
import com.appspot.relaxe.value.VarcharHolder;

public final class VarcharAttribute<
	A extends AttributeName,	
	E extends HasVarchar.Read<A, E, W> & HasString.Read<A, E, W>,
	W extends HasVarchar.Write<A, E, W> & HasString.Write<A, E, W>
	>
	extends StringAttribute<A, E, W, VarcharType, VarcharHolder, VarcharAttribute<A, E, W>>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8770210326767927586L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private VarcharAttribute() {
	}
	
	private VarcharAttribute(A name) {
		super(name);
		
	}	
	
	public static <
		X extends AttributeName,
		T extends HasVarchar.Read<X, T, S> & HasString.Read<X, T, S>,
		S extends HasVarchar.Write<X, T, S> & HasString.Write<X, T, S>
	>
	VarcharAttribute<X, T, S> get(HasVarcharAttribute<X, T, S> meta, X a) {
		VarcharAttribute<X, T, S> k = meta.getVarcharAttribute(a);
		
		if (k == null) {
			if (VarcharType.TYPE.equals(a.type())) {
				k = new VarcharAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
	
	
	@Override
	public VarcharType type() {
		return VarcharType.TYPE;
	}
	
	@Override
	public void set(W e, VarcharHolder newValue) {
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
	public void copy(E src, W dest) {
		dest.setVarchar(this, src.getVarchar(this));
	}
	
	@Override
	public VarcharAttribute<A, E, W> self() {	
		return this;
	}
	
	@Override
	public VarcharHolder as(ValueHolder<?, ?, ?> holder) {
		return VarcharHolder.of(holder);
	}
}

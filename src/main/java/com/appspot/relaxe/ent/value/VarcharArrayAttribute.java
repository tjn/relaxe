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
import com.appspot.relaxe.types.VarcharArrayType;
import com.appspot.relaxe.types.VarcharType;
import com.appspot.relaxe.value.ArrayHolder;
import com.appspot.relaxe.value.StringArray;
import com.appspot.relaxe.value.ValueHolder;
import com.appspot.relaxe.value.VarcharArrayHolder;


public class VarcharArrayAttribute<
	A extends AttributeName,
	E extends HasVarcharArray<A, E>	
>
	extends AbstractArrayAttribute<A, E, String, StringArray, VarcharType, VarcharArrayType, VarcharArrayHolder, VarcharArrayAttribute<A,E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected VarcharArrayAttribute() {
	}
		
	private VarcharArrayAttribute(HasVarcharArrayAttribute<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	@Override
	public VarcharArrayAttribute<A, E> self() {
		return this;
	}

	@Override
	public VarcharArrayType type() {
		return VarcharArrayType.TYPE;
	}

	@Override
	public VarcharArrayHolder newHolder(StringArray content) {
		return new VarcharArrayHolder(content);
	}

	public static <
		X extends AttributeName,
		T extends HasVarcharArray<X, T>
	>
	VarcharArrayAttribute<X, T> get(HasVarcharArrayAttribute<X, T> meta, X a) {
		VarcharArrayAttribute<X, T> k = meta.getVarcharArrayAttribute(a);
		
		if (k == null) {						
			if (VarcharArrayType.TYPE.equals(a.type())) {
				k = new VarcharArrayAttribute<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public VarcharArrayHolder get(E e) throws EntityRuntimeException {
		return e.getVarcharArray(this);
	}

	@Override
	public void set(E e, VarcharArrayHolder newValue)
			throws EntityRuntimeException {
		e.setVarcharArray(this, newValue);
	}

	@Override
	public VarcharArrayHolder as(ValueHolder<?, ?, ?> holder) {
		ArrayHolder<?, ?, ?, ?, ?> ah = holder.asArrayHolder();
		
		if (ah == null) {
			return null;
		}
		
		if (ah instanceof VarcharArrayHolder) {
			return (VarcharArrayHolder) ah;
		}
		
		return null;
	}
}

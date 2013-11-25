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
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.rpc.VarcharArrayHolder;
import com.appspot.relaxe.types.VarcharArrayType;
import com.appspot.relaxe.types.VarcharType;


public class VarcharArrayKey<
	A extends Attribute,
	E extends HasVarcharArray<A, E>	
>
	extends AbstractArrayKey<A, E, String, StringArray, VarcharType, VarcharArrayType, VarcharArrayHolder, VarcharArrayKey<A,E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected VarcharArrayKey() {
	}
		
	private VarcharArrayKey(HasVarcharArrayKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	@Override
	public VarcharArrayKey<A, E> self() {
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
		X extends Attribute,
		T extends HasVarcharArray<X, T>
	>
	VarcharArrayKey<X, T> get(HasVarcharArrayKey<X, T> meta, X a) {
		VarcharArrayKey<X, T> k = meta.getVarcharArrayKey(a);
		
		if (k == null) {						
			if (VarcharArrayType.TYPE.equals(a.type())) {
				k = new VarcharArrayKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

//	@Override
//	public void set(E e, VarcharArrayHolder newValue)
//			throws EntityRuntimeException {
//		e.setVarcharArray(this, newValue);
//	}
	
	@Override
	public VarcharArrayHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asVarcharArrayHolder();
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
}

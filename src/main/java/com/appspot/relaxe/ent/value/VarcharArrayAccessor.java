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
import com.appspot.relaxe.types.VarcharArrayType;
import com.appspot.relaxe.value.VarcharArrayHolder;
import com.appspot.relaxe.value.StringArray;

public class VarcharArrayAccessor<
	A extends AttributeName,
	R extends HasVarcharArray.Read<A, R, RW>,
	RW extends HasVarcharArray.Read<A, R, RW> & HasVarcharArray.Write<A, R, RW>
>
	extends AbstractAttributeAccessor<A, R, RW, StringArray, VarcharArrayType, VarcharArrayHolder, VarcharArrayAttribute<A, R, RW>> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -2731889274638406123L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private VarcharArrayAccessor() {
	}
	
	public VarcharArrayAccessor(RW target, VarcharArrayAttribute<A, R, RW> k) {
		super(target, k);
	}
	
	@Override
	public VarcharArrayHolder getHolder() {
		return getTarget().getVarcharArray(key());
	}
}
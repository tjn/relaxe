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
package com.appspot.relaxe.value;

import com.appspot.relaxe.ent.value.LongVarBinary;
import com.appspot.relaxe.types.LongVarBinaryType;
import com.appspot.relaxe.types.ValueType;



public class LongVarBinaryHolder
	extends AbstractPrimitiveHolder<LongVarBinary, LongVarBinaryType, LongVarBinaryHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6094556721803575825L;

	public static final LongVarBinaryHolder NULL_HOLDER = new LongVarBinaryHolder();
	
	private LongVarBinary value;
		
	
	public static LongVarBinaryHolder valueOf(LongVarBinary v) {
		if (v == null) {
			return NULL_HOLDER;
		}
		
		return new LongVarBinaryHolder(v);		
	}
	
	public LongVarBinaryHolder(LongVarBinary value) {
		this.value = value;
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected LongVarBinaryHolder() {
	}
	
	
	@Override
	public LongVarBinary value() {
		return this.value;
	}

	@Override
	public LongVarBinaryType getType() {
		return LongVarBinaryType.TYPE;
	}

	@Override
	public int getSqlType() {
		return ValueType.BLOB;
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public LongVarBinaryHolder asLongVarBinaryHolder() {
		return this;
	}
	
	@Override
	public LongVarBinaryHolder self() {
		return this;
	}

	public static LongVarBinaryHolder as(AbstractPrimitiveHolder<?, ?, ?> holder) {
		return holder.asLongVarBinaryHolder();
	}
}

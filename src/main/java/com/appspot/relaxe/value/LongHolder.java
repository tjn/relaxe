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

import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.ValueType;


public class LongHolder
	extends AbstractPrimitiveHolder<Long, LongType, LongHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7183468721515930456L;
	/**
	 * 
	 */
	
	private Long value;	
	public static final LongHolder NULL_HOLDER = new LongHolder();
		
	public static LongHolder valueOf(long v) {
		/**
		 * Possibly cache small longs later.
		 */
		return new LongHolder(v);
	}
	
	public static LongHolder valueOf(Long v) {
		return v == null ? NULL_HOLDER : valueOf(v.intValue());
	}
	
	public LongHolder(long value) {
		this.value = Long.valueOf(value);
	}
	
	protected LongHolder() {		
	}
	
	@Override
	public Long value() {
		return this.value;
	}

	@Override
	public LongType getType() {
		return LongType.TYPE;
	}

	@Override
	public int getSqlType() {
		return ValueType.BIGINT;
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public LongHolder asLongHolder() {
		return this;
	}

	@Override
	public LongHolder self() {
		return this;
	}

	public static LongHolder of(ValueHolder<?, ?, ?> holder) {
		return holder.asLongHolder();
	}
}

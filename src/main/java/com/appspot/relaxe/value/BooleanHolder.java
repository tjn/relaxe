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

import com.appspot.relaxe.types.BooleanType;
import com.appspot.relaxe.types.ValueType;


public class BooleanHolder
	extends AbstractPrimitiveHolder<Boolean, BooleanType, BooleanHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;
	private Boolean value;	
	public static final BooleanHolder NULL_HOLDER = new BooleanHolder();
	public static final BooleanHolder TRUE_HOLDER = new BooleanHolder(true);
	public static final BooleanHolder FALSE_HOLDER = new BooleanHolder(false);
		
	public static BooleanHolder valueOf(boolean v) {
		return v ? TRUE_HOLDER : FALSE_HOLDER;
	}
	
	public static BooleanHolder valueOf(Boolean v) {
		return (v == null) ? NULL_HOLDER : valueOf(v.booleanValue());
	}
			
	public BooleanHolder(boolean value) {
		this.value = Boolean.valueOf(value);
	}
	
	protected BooleanHolder() {		
	}
	
	@Override
	public Boolean value() {
		return this.value;
	}

	@Override
	public BooleanType getType() {
		return BooleanType.TYPE;
	}

	@Override
	public int getSqlType() {
		return ValueType.BIT;
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public BooleanHolder asBooleanHolder() {
		return this;
	}
	
	@Override
	public BooleanHolder self() {
		return this;
	}

	public static BooleanHolder as(AbstractPrimitiveHolder<?, ?, ?> holder) {
		return holder.asBooleanHolder();
	}

}

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

import com.appspot.relaxe.types.DoubleType;
import com.appspot.relaxe.types.ValueType;


public class DoubleHolder
	extends AbstractValueHolder<Double, DoubleType, DoubleHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -8533627613502905762L;
	/**
	 * 
	 */	
	private Double value;	
	public static final DoubleHolder NULL_HOLDER = new DoubleHolder();
	public static final DoubleHolder ZERO = new DoubleHolder(0);
	public static final DoubleHolder ONE = new DoubleHolder(1);
	
	public static DoubleHolder valueOf(double v) {
		return 
			(v == 0) ? ZERO : 
			(v == 1) ? ONE :
			DoubleHolder.valueOf(Double.valueOf(v));
	}
	
	public static DoubleHolder valueOf(Double v) {
		return (v == null) ? NULL_HOLDER : new DoubleHolder(v);
	}
	
	public DoubleHolder(double value) {
		this.value = Double.valueOf(value);		
	}
	
	protected DoubleHolder() {		
	}
	
	@Override
	public Double value() {
		return this.value;
	}

	@Override
	public DoubleType getType() {
		return DoubleType.TYPE;
	}

	@Override
	public int getSqlType() {
		return ValueType.INTEGER;
	}

	@Override
	public DoubleHolder self() {
		return this;
	}
	
	@Override
	public DoubleHolder asDoubleHolder() {
		return this;
	}

	public static DoubleHolder of(ValueHolder<?, ?, ?> holder) {
		return holder.asDoubleHolder();
	}
}

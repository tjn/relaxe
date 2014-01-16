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

import java.io.Serializable;

import com.appspot.relaxe.types.AbstractValueType;
import com.appspot.relaxe.types.ValueType;

public abstract class AbstractValueHolder<
	V extends Serializable, 
	T extends ValueType<T>, 
	H extends ValueHolder<V, T, H>
>
	extends AbstractHolder<V, T, H>
	implements ValueHolder<V, T, H>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2373967913129102220L;
	
	
	@Override
	public int getSqlType() {
		return getType().getSqlType();
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + getType().getSqlType() + "]: " + this.value();
	}
	
	// public abstract H self();
	
	
	public H as(AbstractValueType<?> type) {
		return getType().equals(type) ? self() : null;
	}	
	
	/**
	 * If this holder is an {@link IntegerHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	@Override
	public IntegerHolder asIntegerHolder() {
		return null;
	}
	
	@Override
	public DoubleHolder asDoubleHolder() {
		return null;
	}
	
	/**
	 * If this holder is an {@link VarcharHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */
	@Override
	public VarcharHolder asVarcharHolder() {
		return null;
	}
	
	@Override
	public StringHolder<?, ?> asStringHolder() {
		return null;
	}
	
	@Override
	public CharHolder asCharHolder() {
		return null;
	}

	@Override
	public DateHolder asDateHolder() {
		return null;
	}
	
	@Override
	public DecimalHolder asDecimalHolder() {
		return null;
	}

	@Override
	public TimestampHolder asTimestampHolder() {
		return null;
	}

	@Override
	public TimeHolder asTimeHolder() {
		return null;
	}
	
	@Override
	public LongHolder asLongHolder() {
		return null;
	}
	
	@Override
	public BooleanHolder asBooleanHolder() {
		return null;
	}	


	@Override
	public OtherHolder<?, ?, ?> asOtherHolder(String typeName) {
		return null;
	}
	
	@Override
	public ArrayHolder<?, ?, ?, ?, ?> asArrayHolder(String typeName) {
		return null;
	}
	
	@Override
	public LongVarBinaryHolder asLongVarBinaryHolder() {
		return null;
	}	

	/**
	 * If this holder is an {@link IntervalHolder.DayTime}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	@Override
	public IntervalHolder.DayTime asDayTimeIntervalHolder() {
		return null;
	}
	
	/**
	 * If this holder is an {@link IntervalHolder.YearMonth}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	@Override
	public IntervalHolder.YearMonth asYearMonthIntervalHolder() {
		return null;
	}
	

	@Override
	public VarcharArrayHolder asVarcharArrayHolder() {
		return null;
	}
}

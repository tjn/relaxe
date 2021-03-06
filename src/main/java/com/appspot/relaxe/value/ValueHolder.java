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

import com.appspot.relaxe.InputStreamKeyHolder;
import com.appspot.relaxe.types.ValueType;

public interface ValueHolder<
	V extends Serializable, 
	T extends ValueType<T>, 
	H extends ValueHolder<V, T, H>
> 
	extends Holder<V, T, H>, Serializable
	{
		
	public int getSqlType();
	
	@Override
	H self();
	
	/**
	 * If this holder is an;@link IntegerHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */		
	IntegerHolder asIntegerHolder();
	
	DoubleHolder asDoubleHolder();
	
	/**
	 * If this holder is an;@link VarcharHolder}, returns itself as such. Otherwise, returns <code>null</code> 
	 */
	VarcharHolder asVarcharHolder();

	CharHolder asCharHolder();

	DateHolder asDateHolder();
	
	DecimalHolder asDecimalHolder();

	TimestampHolder asTimestampHolder();

	TimeHolder asTimeHolder();
	
	LongHolder asLongHolder();
	
	BooleanHolder asBooleanHolder();
		
	StringHolder<?, ?> asStringHolder();

	OtherHolder<?, ?, ?> asOtherHolder(String typeName);
	
	ArrayHolder<?, ?, ?, ?, ?> asArrayHolder();
	
	LongVarBinaryHolder asLongVarBinaryHolder();
	
	IntervalHolder.DayTime asDayTimeIntervalHolder();
	
	IntervalHolder.YearMonth asYearMonthIntervalHolder();
	
	InputStreamKeyHolder asInputStreamKeyHolder();
}

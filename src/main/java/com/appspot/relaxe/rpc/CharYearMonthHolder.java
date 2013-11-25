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
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.CharType;
import com.appspot.relaxe.types.CharYearMonthIntervalType;
import com.appspot.relaxe.types.IntervalType;

public class CharYearMonthHolder
	extends VirtualHolder<Interval.YearMonth, String, CharType, CharHolder, IntervalType.YearMonth, CharYearMonthIntervalType, CharYearMonthHolder> {
	
	private CharHolder implementedAs;
	
	public CharYearMonthHolder(Interval.YearMonth v) {
		super(v);
		implementedAs = (v == null) ? CharHolder.NULL_HOLDER : CharHolder.valueOf(v.toString());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7671379434861527849L;
	
	@Override
	public CharYearMonthIntervalType getType() {		
		return CharYearMonthIntervalType.TYPE;
	}

	@Override
	public CharHolder implementedAs() {
		return this.implementedAs;
	}

	@Override
	public CharYearMonthHolder self() {
		return this;
	}
}

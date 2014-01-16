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

import java.util.Date;

import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.TimeType;


public class TimeHolder
	extends AbstractValueHolder<Date, TimeType, TimeHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 7452231603391333418L;
	
	private Date value;	
	public static final TimeHolder NULL_HOLDER = new TimeHolder();
	
	public static TimeHolder valueOf(Integer v) {
		return v == null ? NULL_HOLDER : valueOf(v.intValue());
	}
	
	public TimeHolder(Date value) {
		this.value = new Date(value.getTime());
	}
	
	public static TimeHolder valueOf(Date value) {
		return (value == null) ? NULL_HOLDER : new TimeHolder(value);
	}
	
	private TimeHolder() {		
	}
	
	@Override
	public Date value() {
		return (this.value == null) ? null : new Date(this.value.getTime());
	}	
		
	@Override
	public TimeType getType() {
		return TimeType.TYPE;
	}
	
	@Override
	public int getSqlType() {
		return ValueType.TIME;
	}
	
	@Override
	public TimeHolder asTimeHolder() {
		return this;
	}

	@Override
	public TimeHolder self() {
		return this;
	}

	public static TimeHolder of(ValueHolder<?, ?, ?> holder) {
		return holder.asTimeHolder();
	}
}

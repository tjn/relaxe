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

import com.appspot.relaxe.types.DateType;



public class DateHolder
	extends AbstractPrimitiveHolder<Date, DateType, DateHolder> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 8724793570826638205L;
	
	private Date value;	
	public static final DateHolder NULL_HOLDER = new DateHolder();
		
	public DateHolder(Date value) {
		this.value = (value == null) ? null : new Date(value.getTime());
	}
	
	public static DateHolder valueOf(Date value) {
		return (value == null) ? NULL_HOLDER : new DateHolder(value);
	}
	
	public static DateHolder currentDate() {
		return new DateHolder(new Date());
	}
	
	
	private DateHolder() {		
	}
	
	@Override
	public Date value() {
		return (this.value == null) ? null : new Date(this.value.getTime());
	}
		
	@Override
	public DateType getType() {
		return DateType.TYPE;
	}
	
	@Override
	public DateHolder asDateHolder() {
		return this;
	}

	@Override
	public DateHolder self() {
		return this;
	}
	
	public static DateHolder as(ValueHolder<?, ?, ?> holder) {
		return holder.asDateHolder();
	}
}

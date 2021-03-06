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
package com.appspot.relaxe.env.hsqldb;

import com.appspot.relaxe.types.VarcharType;
import com.appspot.relaxe.value.ArrayValue;
import com.appspot.relaxe.value.StringArrayHolder;
import com.appspot.relaxe.value.ValueHolder;

public class HSQLDBTextArrayHolder
	extends StringArrayHolder<VarcharType, HSQLDBTextArrayType, HSQLDBTextArrayHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1432615933967175087L;
	
	public static final HSQLDBTextArrayHolder NULL_HOLDER = new HSQLDBTextArrayHolder();
	
	public HSQLDBTextArrayHolder() {
		super();
	}

	public HSQLDBTextArrayHolder(String[] data) {
		super(data);	
	}
	
	public HSQLDBTextArrayHolder(ArrayValue<String> value) {
		super(value);
	}

	@Override
	public HSQLDBTextArrayHolder self() {
		return this;
	}

	@Override
	public HSQLDBTextArrayType getType() {
		return HSQLDBTextArrayType.TYPE;
	}

	public static HSQLDBTextArrayHolder valueOf(ArrayValue<String> value) {
		return (value == null) ? NULL_HOLDER : new HSQLDBTextArrayHolder(value);
	}

	public static HSQLDBTextArrayHolder of(ValueHolder<?, ?, ?> holder) {
		Object h = holder.self();
		HSQLDBTextArrayHolder mh = (HSQLDBTextArrayHolder) h;	
		return mh;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof HSQLDBTextArrayHolder)) {
			return false;
		}
		
		HSQLDBTextArrayHolder h = (HSQLDBTextArrayHolder) obj;		
		
		return contentEquals(h);		
	}
	
}

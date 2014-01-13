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

import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.ValueType;

public abstract class StringArrayHolder<
	E extends ValueType<E>, 
	T extends ArrayType<T, E>, 
	H extends StringArrayHolder<E, T, H>
>
	extends ArrayHolder<String, StringArray, E, T, H> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4156368459784864451L;
	
	private String[] content = {};

	public StringArrayHolder() {		
	}

	public StringArrayHolder(String[] data) {
		this.content = StringArray.copy(data);
	}
	
	public StringArrayHolder(ArrayValue<String> value) {
		this.content = value.toArray();
	}
	
	public StringArrayHolder(H value) {
		StringArrayHolder<E, T, H> h = value;
		this.content = h.content;
	}
	
	public String[] getContent() {
		return StringArray.copy(content);
	}	
}

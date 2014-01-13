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
package com.appspot.relaxe.expr;

import java.io.Serializable;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;


public class MutableValueParameter<
	V extends Serializable,
	T extends ValueType<T>, 
	H extends ValueHolder<V, T, H>
>
	extends AbstractParameter<V, T, H> 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8609263476546435725L;
	
	private H value;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private MutableValueParameter() {
	}
	
	@Override
	public H getValue() {
		return this.value;
	}
	
	public MutableValueParameter(Column column, H value) {
		super(column);
		setValue(value);		
	}

	public void setValue(H value) {
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		this.value = value;
	}
	
	@Override
	public boolean isMutable() {
		return true;
	}
}

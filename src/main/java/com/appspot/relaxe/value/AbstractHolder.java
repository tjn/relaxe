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

import com.appspot.relaxe.types.Type;


/** 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public abstract class AbstractHolder<
	V extends Serializable, 
	T extends Type<T>, 
	H extends Holder<V, T, H>
>
	implements Holder<V, T, H>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127849751940907829L;	
	
	
	@Override
	public boolean isNull() {
		return value() == null;
	}
	
	@Override
	public abstract T getType();
	
	/**
	 * 
	 * @param holder (must not be null)
	 * 
	 * @return
	 */
	@Override
	public boolean contentEquals(H holder) {
		V a = value();
		V b = holder.value();		
		return (a == null) ? (b == null) : a.equals(b);
	}
	

	@Override
	public abstract boolean equals(Object obj);
		 
	
	@Override
	public int hashCode() {
		V v = value();
		int vh = (v == null) ? 0 : v.hashCode();
		int th = getType().hashCode();		
		return vh ^ th;
	}
}

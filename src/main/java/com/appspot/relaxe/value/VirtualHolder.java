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

import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.VirtualType;



/**
 * 
 * @author tnie
 *
 * @param <S> AbstractType of the virtualized value.
 * @param <T> AbstractType used to implement <code>S</code>.
 * @param <I> Primitive type corresponding <code>T</code>
 * @param <H> Primitive holder corresponding <code>T</code> and <code>I</code>.  
 * @param <V> Primitive type which represent the type of this class.
 * @param <Z> Subclassed virtual holder.
 */
public abstract class VirtualHolder<
	S extends Serializable,
	T extends Serializable,
	I extends ValueType<I>,
	H extends ValueHolder<T, I, H>,
	V extends ValueType<V>,
	Z extends VirtualType<Z, I, V>,
	VH extends VirtualHolder<S, T, I, H, V, Z, VH>
>
	extends AbstractValueHolder<S, Z, VH> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1151472831021160920L;	
	private S value;
	
	public VirtualHolder(S newValue) {
		this.value = newValue;		
	}
			
	public abstract H implementedAs();
	
	@Override
	public S value() {	
		return this.value;
	}
	
	@Override
	public int getSqlType() {
		return getType().getSqlType();
	}
}

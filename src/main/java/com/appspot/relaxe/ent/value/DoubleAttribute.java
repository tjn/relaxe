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
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.types.DoubleType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.DoubleHolder;
import com.appspot.relaxe.value.ValueHolder;

public final class DoubleAttribute<
	A extends AttributeName,
	E extends HasDouble.Read<A, E, B>,
	B extends HasDouble.Write<A, E, B>
	>
	extends AbstractAttribute<A, E, B, Double, DoubleType, DoubleHolder, DoubleAttribute<A, E, B>>	
	{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DoubleAttribute() {
	}
	
	private DoubleAttribute(A name) {
		super(name);		
	}
	
	public static <
		X extends AttributeName,
		T extends HasDouble.Read<X, T, S>,
		S extends HasDouble.Write<X, T, S>
	>
	DoubleAttribute<X, T, S> get(HasDoubleAttribute<X, T, S> meta, X a) {
		DoubleAttribute<X, T, S> k = meta.getDoubleAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (t != null && DoubleType.TYPE.equals(t)) {
				k = new DoubleAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
		
	@Override
	public DoubleType type() {
		return DoubleType.TYPE;
	}
	
	@Override
	public void set(B e, DoubleHolder newValue) {
		e.setDouble(this, newValue);
	}
	
	@Override
	public DoubleHolder get(E e) {
		return e.getDouble(self());
	}
	
	@Override
	public DoubleHolder newHolder(Double newValue) {
		return DoubleHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, B dest) {
		dest.setDouble(this, src.getDouble(this));
	}
	
	@Override
	public DoubleAttribute<A, E, B> self() {
		return this;
	}
	
	@Override
	public void reset(B dest) throws EntityRuntimeException {
		dest.setDouble(this, DoubleHolder.NULL_HOLDER);
	}
		
	@Override
	public DoubleHolder as(ValueHolder<?, ?, ?> unknown) {
		return unknown.asDoubleHolder();
	}
}

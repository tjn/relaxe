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
import com.appspot.relaxe.types.DecimalType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.Decimal;
import com.appspot.relaxe.value.DecimalHolder;
import com.appspot.relaxe.value.ValueHolder;

public final class DecimalAttribute<
	A extends AttributeName,
	E extends HasDecimal.Read<A, E, B>,
	B extends HasDecimal.Write<A, E, B>
>
extends AbstractAttribute<A, E, B, Decimal, DecimalType, DecimalHolder, DecimalAttribute<A, E, B>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DecimalAttribute() {
	}
	
	private DecimalAttribute(A name) {
		super(name);		
	}
	
	public static <
		X extends AttributeName,
		T extends HasDecimal.Read<X, T, S>,
		S extends HasDecimal.Write<X, T, S>
	>
	DecimalAttribute<X, T, S> get(HasDecimalAttribute<X, T, S> meta, X a) {
		DecimalAttribute<X, T, S> k = meta.getDecimalAttribute(a);
		
		if (k == null) {
			ValueType<?> t = a.type();
			
			if (t != null && DecimalType.TYPE.equals(t)) {
				k = new DecimalAttribute<X, T, S>(a);
				meta.register(k);
			}
		}
				
		return k;
	}
		
	@Override
	public DecimalType type() {
		return DecimalType.TYPE;
	}
	
	@Override
	public void set(B e, DecimalHolder newValue) {
		e.setDecimal(this, newValue);
	}
	
	@Override
	public DecimalHolder get(E e) {
		return e.getDecimal(self());
	}
	
	@Override
	public DecimalHolder newHolder(Decimal newValue) {
		return DecimalHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, B dest) {
		dest.setDecimal(this, src.getDecimal(this));
	}
	
	@Override
	public DecimalAttribute<A, E, B> self() {
		return this;
	}
	
	@Override
	public void reset(B dest) throws EntityRuntimeException {
		dest.setDecimal(this, DecimalHolder.NULL_HOLDER);
	}
		
	@Override
	public DecimalHolder as(ValueHolder<?, ?, ?> unknown) {
		return unknown.asDecimalHolder();
	}
}

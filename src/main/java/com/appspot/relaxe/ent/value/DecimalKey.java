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
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.Decimal;
import com.appspot.relaxe.rpc.DecimalHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.DecimalType;

public final class DecimalKey<
	A extends Attribute,
	E extends HasDecimal<A, E>
>
	extends AbstractPrimitiveKey<A, E, Decimal, DecimalType, DecimalHolder, DecimalKey<A, E>>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1332513199153161810L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DecimalKey() {
	}

	private DecimalKey(HasDecimalKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,		
		T extends HasDecimal<X, T>
	>
	DecimalKey<X, T> get(HasDecimalKey<X, T> meta, X a) {
		DecimalKey<X, T> k = meta.getDecimalKey(a);
		
		if (k == null) {
			if (DecimalType.TYPE.equals(a.type())) {
				k = new DecimalKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DecimalType type() {
		return DecimalType.TYPE;
	}

	@Override
	public void set(E e, DecimalHolder newValue)
		throws EntityRuntimeException {
		e.setDecimal(this, newValue);
	}
	
	@Override
	public DecimalHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDecimal(this);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDecimal(this, src.getDecimal(this));
	}

	@Override
	public DecimalHolder newHolder(Decimal newValue) {
		return DecimalHolder.valueOf(newValue);
	}
	
	@Override
	public DecimalKey<A, E> self() {
		return this;
	}
	
	@Override
	public DecimalHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return DecimalHolder.of(holder);
	}
}

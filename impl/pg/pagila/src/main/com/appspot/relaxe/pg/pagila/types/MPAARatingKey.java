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
package com.appspot.relaxe.pg.pagila.types;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.value.AbstractOtherKey;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.OtherType;
import com.appspot.relaxe.types.PrimitiveType;

public class MPAARatingKey<
	A extends Attribute,
	E extends HasMPAARating<A, E>	
>
	extends AbstractOtherKey<A, E, MPAARating, MPAARatingType, MPAARatingHolder, MPAARatingKey<A, E>>
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private MPAARatingKey() {
	}
	
	private MPAARatingKey(HasMPAARatingKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasMPAARating<X, T>
	>
	MPAARatingKey<X, T> get(HasMPAARatingKey<X, T> meta, X a) {
		MPAARatingKey<X, T> k = meta.getMPAARatingKey(a);
		
		if (k == null) {
			MPAARatingType kt = MPAARatingType.TYPE;
			OtherType<?> t = a.type().asOtherType();
			
			if (t != null && t.getSqlType() == PrimitiveType.OTHER && kt.getName().equals(t.getName())) {
				k = new MPAARatingKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}	

			
	@Override
	public MPAARatingType type() {
		return MPAARatingType.TYPE;
	}
	
	@Override
	public void set(E e, MPAARatingHolder newValue) 
		throws EntityRuntimeException {
		e.setMPAARating(this, newValue);
	}
	
	@Override
	public MPAARatingHolder get(E e) 
		throws EntityRuntimeException {
		return e.getMPAARating(this);
	}
	
	@Override
	public MPAARatingHolder newHolder(MPAARating newValue) {
		return MPAARatingHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setMPAARating(this, src.getMPAARating(this));
	}

	@Override
	public MPAARatingKey<A, E> self() {
		return this;
	}

	@Override
	public MPAARatingHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return MPAARatingHolder.of(holder);
	}
}

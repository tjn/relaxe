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
package com.appspot.relaxe.common.pagila.types;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.value.AbstractOtherAttribute;
import com.appspot.relaxe.types.OtherType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;


public class MPAARatingAttribute<
	A extends AttributeName,
	R extends HasMPAARating.Read<A, R, W>,	
	W extends HasMPAARating.Write<A, R, W>
>
	extends AbstractOtherAttribute<A, R, W, MPAARating, MPAARatingType, MPAARatingHolder, MPAARatingAttribute<A, R, W>>
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -572611877782044774L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private MPAARatingAttribute() {
	}
	
	private MPAARatingAttribute(A name) {
		super(name);		
	}
	
	public static <
		X extends AttributeName,
		T extends HasMPAARating.Read<X, T, S>,
		S extends HasMPAARating.Write<X, T, S>		
	>
	MPAARatingAttribute<X, T, S> get(HasMPAARatingAttribute<X, T, S> meta, X a) {
		MPAARatingAttribute<X, T, S> k = meta.getMPAARatingAttribute(a);
		
		if (k == null) {
			MPAARatingType kt = MPAARatingType.TYPE;
			OtherType<?> t = a.type().asOtherType();
			
			if (t != null && t.getSqlType() == ValueType.OTHER && kt.getName().equals(t.getName())) {
				k = new MPAARatingAttribute<X, T, S>(a);
				meta.register(k);
			}			
		}
				
		return k;
	}	

			
	@Override
	public MPAARatingType type() {
		return MPAARatingType.TYPE;
	}
	
	@Override
	public void set(W e, MPAARatingHolder newValue) {
		e.setMPAARating(this, newValue);
	}
	
	@Override
	public MPAARatingHolder get(R e) {
		return e.getMPAARating(this);
	}
	
	@Override
	public MPAARatingHolder newHolder(MPAARating newValue) {
		return MPAARatingHolder.valueOf(newValue);
	}

	@Override
	public void copy(R src, W dest) {
		dest.setMPAARating(this, src.getMPAARating(this));
	}

	@Override
	public MPAARatingAttribute<A, R, W> self() {
		return this;
	}

	@Override
	public MPAARatingHolder as(ValueHolder<?, ?, ?> holder) {
		return MPAARatingHolder.of(holder);
	}
}

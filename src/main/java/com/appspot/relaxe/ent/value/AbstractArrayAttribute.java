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

import java.io.Serializable;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ArrayHolder;
import com.appspot.relaxe.value.ArrayValue;


public abstract class AbstractArrayAttribute<
	A extends AttributeName,
	E,
	W,
	O extends Serializable,
	V extends ArrayValue<O>,
	C extends ValueType<C>,
	P extends ArrayType<P, C>,	 
	H extends ArrayHolder<O, V, C, P, H>,
	K extends AbstractArrayAttribute<A, E, W, O, V, C, P, H, K>
	>
	extends AbstractAttribute<A, E, W, V, P, H, K>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected AbstractArrayAttribute() {
	}
	
	protected AbstractArrayAttribute(A name) {
		super(name);	
	}

	@Override
	public abstract K self();
}

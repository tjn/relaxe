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
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;


public interface ValueAttribute<
	A extends AttributeName,	
	E,
	V extends Serializable,
	P extends ValueType<P>,
	H extends ValueHolder<V, P, H>,	
	K extends ValueAttribute<A, E, V, P, H, K>
>
	extends Key<E, P, K>, Serializable
{
	@Override
	P type();
	A name();
	H newHolder(V newValue);
	H get(E e) throws EntityRuntimeException;
	void set(E e, H newValue) throws EntityRuntimeException;	
	@Override
	void copy(E src, E dest) throws EntityRuntimeException;
	
	
	/**
	 * Assign the default value (the value of the expression <code>newHolder(null)</code>) for the attribute addressed by this key.
	 *  
	 * @param dest
	 */
	@Override
	void reset(E dest) throws EntityRuntimeException;
		
	@Override
	K self();
		
	/**
	 * If the type of the given holder is the same type type of this key, returns holder as a holder of type <code>H</code>. 
	 * Otherwise, returns <code>null</code>. 
	 *  
	 * @param holder
	 * 
	 * @throws NullPointerException if <code>holder</code> is <code>null</code>.
	 */
	H as(ValueHolder<?, ?, ?> holder);

}

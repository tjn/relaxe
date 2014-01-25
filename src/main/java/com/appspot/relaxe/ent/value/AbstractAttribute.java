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
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;


public abstract class AbstractAttribute<
	A extends AttributeName,
	E,
	V extends Serializable,
	P extends ValueType<P>,
	H extends ValueHolder<V, P, H>,
	K extends Attribute<A, E, V, P, H, K>
>
	implements Attribute<A, E, V, P, H, K> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -3422141375386521175L;
	private A name;
	
	protected AbstractAttribute() {		
	}	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected AbstractAttribute(A name) {
		setName(name);
	}
		

	private void setName(A name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;
	}

	@Override
	public A name() {
		return this.name;
	}	

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			throw new NullPointerException("o");
		}
		
		if (o == this) {
			return true;
		}
		
		if(!getClass().equals(o.getClass())) {
			return false;
		}
		
		// Since getClass().equals(o.getClass()) implies t.type().getSqlType() == type().getSqlType()
		// we only need to check the name:		
		AbstractAttribute<?, ?, ?, ?, ?, ?> t = (AbstractAttribute<?, ?, ?, ?, ?, ?>) o;				
		return nameEquals(t);
	}
	
	private boolean nameEquals(AbstractAttribute<?, ?, ?, ?, ?, ?> pk) {		
		return name().equals(pk.name());
	}
	
	@Override
	public void copy(E src, E dest) {		
		set(dest, get(src));		
	}
	
	@Override
	public void reset(E dest) {
		set(dest, newHolder(null));		
	}
}

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
package com.appspot.relaxe.ent.im;

import java.io.Serializable;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;


public class AbstractKeyIdentityMap<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, ?, M>,
	E extends Entity<A, R, T, E, B, H, ?, M>,
	B extends MutableEntity<A, R, T, E, B, H, ?, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	M extends EntityMetaData<A, R, T, E, B, H, ?, M>,
	V extends Serializable,
	P extends ValueType<P>,
	X extends ValueHolder<V, P, X>,	
	K extends Attribute<A, E, ?, V, P, X, K>
>
	extends AbstractIdentityMap<A, R, T, E, H, M, V>
{
	private K key;
	
	public AbstractKeyIdentityMap(K key) {
		super();
		
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		this.key = key;
	}
	
	@Override
	protected V getIdentityKey(E src) {
		X h = key.get(src);		
		return (h == null) ? null : h.value();
	}
}

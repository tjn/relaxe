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
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.types.ReferenceType;


/**
 *
 *
 * @param <A>
 * @param <R>
 * @param <T>
 * @param <V>
 */

public abstract class ReferenceHolder<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, V, H, ?, M>,
	V extends Entity<A, R, T, V, H, ?, M>,
	H extends ReferenceHolder<A, R, T, V, H, M>,
	M extends EntityMetaData<A, R, T, V, H, ?, M>
>
	extends AbstractHolder<V, T, H> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7758303666346608268L;
	private V value;

	protected ReferenceHolder() {
		super();
	}

	public ReferenceHolder(V value) {
		super();
		this.value = value;
	}

	@Override
	public V value() {
		return this.value;
	}
	
		
//	public boolean identityEquals(H h) {
//		if (h == null) {
//			throw new NullPointerException("h");
//		}
//		
//		if (this.isNull()) {
//			return h.isNull();
//		}
//	}
}

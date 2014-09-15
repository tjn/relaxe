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
package com.appspot.relaxe.ent;

import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public abstract class DefaultImmutableEntity<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> extends DefaultEntity<A, R, T, E, B, H, F, M> {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -734191010873474350L;

	@Override
	public final boolean isMutable() {	
		return false;
	}
	
	@Override
	public final B asMutable() {
		return null;
	}
	
	@Override
	public final E toImmutable() {
		return self();
	}
	
	@Override
	public E toImmutable(Operation.Context ctx) {
		return self();
	}

	
	@Override
	public B toMutable() {
		// FIXME: traverse whole graph and instantiate exactly one mutable entity for each traversed entity    
		
		M meta = getMetaData();
		F ef = meta.getFactory();				
		E src = self(); 
		B dest = ef.newEntity();
		
		for (A a : meta.attributes()) {
			Attribute<A, E, B, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.copy(src, dest);
		}
		
		for (R r : meta.relationships()) {
			EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
			ek.copy(src, dest);			
		}
		
		return dest;				
	}
	
	

}

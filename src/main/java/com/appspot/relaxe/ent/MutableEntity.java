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

import java.io.Serializable;

import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.HasString;
import com.appspot.relaxe.ent.value.StringAttribute;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.StringHolder;
import com.appspot.relaxe.value.ValueHolder;

public interface MutableEntity<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>
	extends Entity<A, R, T, E, B, H, F, M>, HasString.Write<A, E, B> {

	<
		S extends Serializable,
		P extends ValueType<P>,
		RH extends ValueHolder<S, P, RH>,
		K extends Attribute<A, E, B, S, P, RH, K>
	>
	void set(K k, RH newValue);


	<
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>
	>
	void setRef(EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K> k, RH newValue);

	@Override
	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringAttribute<A, E, B, P, SH, K>
	>
	void setString(K k, SH s) throws EntityRuntimeException;

	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringAttribute<A, E, B, P, SH, K>
	>
	void setString(K k, String s) throws EntityRuntimeException;
	
	
	/**
	 * Replaces the current value of the attribute addressed by key with null-valued holder
	 * @param <VV>
	 * @param <VT>
	 * @param <VH>
	 * @param <K>
	 * @param key
	 */
	public <
		VV extends Serializable,
		VT extends ValueType<VT>,
		VH extends ValueHolder<VV, VT, VH>,
		K extends Attribute<A, E, B, VV, VT, VH, K>
	>
	void remove(K key);

	/**
	 * * Resets the specified attributes.
	 */
	void reset(Iterable<A> attributes);
	
		
	@Override
	E toImmutable();
		
	@Override
	E toImmutable(Operation.Context ic);
	
	@Override
	B asMutable();
	
	E as();
}

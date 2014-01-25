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

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;


/**
 * Key to address an entity reference of the type <code>V</code> within an entity of type <code>E</code> 
 * 
 * @author tnie
 *
 * @param <R>
 * @param <T>
 * @param <E>
 * @param <M>
 * @param <RT>
 * @param <RE>
 * @param <RH>
 * @param <RM>
 * @param <K>
 */
public interface EntityKey<
	A extends AttributeName,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	RA extends AttributeName,
	RR extends Reference,	
	RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
	RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
	RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
	RF extends EntityFactory<RE, RH, RM, RF>,
	RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,
	K extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, K>	
>
	extends Key<E, RT, K>
{	
	R name();	
	RH newHolder(RE newValue);	
	RH get(E e);
	RE value(E e);
	void set(E e, RH newValue);
	void set(E e, RE newValue);

	M getSource();	
	RM getTarget();
	@Override
	K self();
	
	
}

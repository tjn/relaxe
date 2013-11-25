/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.service;

import java.util.List;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public interface EntitySession {
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E merge(E e) throws EntityException;
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E insert(E e)
		throws EntityException;
	
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E update(E e)
		throws EntityException;	
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	void delete(E e)
		throws EntityException;

	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E sync(E e) throws EntityException;	
	
	public <
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content,		
	    RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
	>
	EntityQueryResult<A, R, T, E, H, F, M, C, RE> query(EntityQuery<A, R, T, E, H, F, M, C, RE> query, FetchOptions opts)
		throws EntityException;
	
	
	public <
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content,
	    RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>	
	>
	List<E> load(EntityQuery<A, R, T, E, H, F, M, C, RE> q, FetchOptions opts)
			throws EntityException;
	
	/**
	 * 
	 */
	void flush();
	


	public <		
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, ?>,
		E extends Entity<A, R, T, E, H, F, M, ?>,
		H extends ReferenceHolder<A, R, T, E, H, M, ?>,
		F extends EntityFactory<E, H, M, F, ?>,
		M extends EntityMetaData<A, R, T, E, H, F, M, ?>		
	>
	E newEntity(T type);
}

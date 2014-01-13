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
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.DBMetaTestCase;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;


public abstract class AbstractPersistenceManagerTest<I extends Implementation<I>> extends DBMetaTestCase<I>  {
	
	private UnificationContext unificationContext;

	protected <
		A extends AttributeName, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	PersistenceManager<A, R, T, E, H, F, M> create(E e) {
		PersistenceManager<A, R, T, E, H, F, M> pm = 
				new PersistenceManager<A, R, T, E, H, F, M>(e, getPersistenceContext(), getUnificationContext());
		return pm;
	}
		
	protected <
		A extends AttributeName, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	E merge(E e, PersistenceContext<?> pctx, Connection c) 
			throws EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M> pm = 
				new PersistenceManager<A, R, T, E, H, F, M>(e, pctx, getUnificationContext());
		pm.merge(c);
		return e;
	}
	
	
	protected <
		A extends AttributeName, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	E sync(E e, PersistenceContext<?> pctx, Connection c) throws EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M> pm = 
				new PersistenceManager<A, R, T, E, H, F, M>(e, pctx, getUnificationContext());
		return pm.sync(c);		
	}

	
	protected <
		A extends AttributeName, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	void delete(E e, PersistenceContext<?> pctx, Connection c) throws EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M> pm = 
				new PersistenceManager<A, R, T, E, H, F, M>(e, pctx, getUnificationContext());
		pm.delete(c);		
	}
	
	protected <
		A extends AttributeName, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>,
		Q extends EntityQuery<A, R, T, E, H, F, M, RE>
	>
	E merge(E e) throws EntityException, SQLException, QueryException {		
		return merge(e, getPersistenceContext(), getConnection());		
	}
	
	protected <
		A extends AttributeName, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,		
		M extends EntityMetaData<A, R, T, E, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>,
		Q extends EntityQuery<A, R, T, E, H, F, M, RE>
	>
	void delete(Entity<A, R, T, E, H, F, M> e) throws EntityException, SQLException, QueryException {
		delete(e.self(), getPersistenceContext(), getConnection());		
	}

	protected UnificationContext getUnificationContext() {
		return unificationContext;
	}

	protected void setUnificationContext(UnificationContext unificationContext) {
		this.unificationContext = unificationContext;
	}
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.CyclicTemplateException;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.DBMetaTestCase;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public abstract class AbstractPersistenceManagerTest<I extends Implementation<I>> extends DBMetaTestCase<I>  {
	
	private UnificationContext unificationContext;

	protected <
		A extends Attribute, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	PersistenceManager<A, R, T, E, H, F, M, C> create(E e) {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, getPersistenceContext(), getUnificationContext());
		return pm;
	}
		
	protected <
		A extends Attribute, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	E merge(E e, PersistenceContext<?> pctx, Connection c) throws CyclicTemplateException, EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, pctx, getUnificationContext());
		pm.merge(c);
		return e;
	}
	
	
	protected <
		A extends Attribute, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	E sync(E e, PersistenceContext<?> pctx, Connection c) throws CyclicTemplateException, EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, pctx, getUnificationContext());
		return pm.sync(c);		
	}

	
	protected <
		A extends Attribute, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void delete(E e, PersistenceContext<?> pctx, Connection c) throws EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, pctx, getUnificationContext());
		pm.delete(c);		
	}
	
	protected <
		A extends Attribute, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	E merge(E e) throws CyclicTemplateException, EntityException, SQLException, QueryException {		
		return merge(e, getPersistenceContext(), getConnection());		
	}
	
	protected <
		A extends Attribute, 
		R extends com.appspot.relaxe.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void delete(E e) throws EntityException, SQLException, QueryException {
		delete(e, getPersistenceContext(), getConnection());		
	}

	protected UnificationContext getUnificationContext() {
		return unificationContext;
	}

	protected void setUnificationContext(UnificationContext unificationContext) {
		this.unificationContext = unificationContext;
	}
}

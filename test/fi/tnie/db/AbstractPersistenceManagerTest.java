/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.CyclicTemplateException;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.meta.DBMetaTestCase;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractPersistenceManagerTest<I extends Implementation<I>> extends DBMetaTestCase<I>  {
	
	private UnificationContext unificationContext;

	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
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
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void merge(E e, PersistenceContext<?> pctx, Connection c) throws CyclicTemplateException, EntityException, SQLException, QueryException {
		PersistenceManager<A, R, T, E, H, F, M, C> pm = new PersistenceManager<A, R, T, E, H, F, M, C>(e, pctx, getUnificationContext());
		pm.merge(c);		
	}
	
	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
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
		R extends fi.tnie.db.ent.Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends Content
	>
	void merge(E e) throws CyclicTemplateException, EntityException, SQLException, QueryException {		
		merge(e, getPersistenceContext(), getConnection());		
	}
	
	protected <
		A extends Attribute, 
		R extends fi.tnie.db.ent.Reference, 
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

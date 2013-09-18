/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.EntityQueryExecutor;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.QueryExecutor;
import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.QueryExpressionSource;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;
import com.appspot.relaxe.service.QuerySession;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.types.ReferenceType;


public abstract class AbstractDataAccessSession<I extends Implementation<I>>
	implements DataAccessSession, EntitySession, QuerySession, StatementSession {
		
	private Connection connection;
	private PersistenceContext<I> persistenceContext;
	private UnificationContext unificationContext;
	
	public AbstractDataAccessSession(PersistenceContext<I> implementation, Connection connection) {
		super();		
		this.persistenceContext = implementation;
		this.connection = connection;
		this.unificationContext = new SimpleUnificationContext();
	}

	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>, 
		H extends ReferenceHolder<A, R, T, E, H, M, C>, 
		F extends EntityFactory<E, H, M, F, C>, 
		M extends EntityMetaData<A, R, T, E, H, F, M, C>, 
		C extends Content,
	    RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>,
		Q extends EntityQuery<A, R, T, E, H, F, M, C, RE>
	> 
	void delete(E e) throws EntityException {						
		PersistenceManager<A,R,T,E,H,F,M,C,RE> pm = new PersistenceManager<A,R,T,E,H,F,M,C,RE>(e, getPersistenceContext(), this.unificationContext);
		pm.delete(this.connection);
	}

	private PersistenceContext<I> getPersistenceContext() {
		return this.persistenceContext;
	}

	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>, 
		H extends ReferenceHolder<A, R, T, E, H, M, C>, 
		F extends EntityFactory<E, H, M, F, C>, 
		M extends EntityMetaData<A, R, T, E, H, F, M, C>, 
		C extends Content,
	    RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>,
		Q extends EntityQuery<A, R, T, E, H, F, M, C, RE>
	> 
	E insert(E e) throws EntityException {
		PersistenceManager<A,R,T,E,H,F,M,C,RE> pm = new PersistenceManager<A,R,T,E,H,F,M,C,RE>(e, getPersistenceContext(), this.unificationContext);
		pm.insert(this.connection);
		return e;
	}
	
	
	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>, 
		H extends ReferenceHolder<A, R, T, E, H, M, C>, 
		F extends EntityFactory<E, H, M, F, C>, 
		M extends EntityMetaData<A, R, T, E, H, F, M, C>, 
		C extends Content,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>,
		Q extends EntityQuery<A, R, T, E, H, F, M, C, RE>
	> 
	E sync(E e) throws EntityException {
		try {
			PersistenceManager<A,R,T,E,H,F,M,C,RE> pm = 
					new PersistenceManager<A,R,T,E,H,F,M,C,RE>(e, getPersistenceContext(), this.unificationContext);
			E result = pm.sync(getConnection());
			return result;
		} 
		catch (SQLException se) {
			throw new EntityException(se.getMessage());
		} 
		catch (QueryException qe) {
			throw new EntityException(qe.getMessage());
		}		
	}

	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>, 
		H extends ReferenceHolder<A, R, T, E, H, M, C>, 
		F extends EntityFactory<E, H, M, F, C>, 
		M extends EntityMetaData<A, R, T, E, H, F, M, C>, 
		C extends Content,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>,
		Q extends EntityQuery<A, R, T, E, H, F, M, C, RE>
	> 
	E merge(E e) throws EntityException {
		try {		
			PersistenceManager<A,R,T,E,H,F,M,C,RE> pm = new PersistenceManager<A,R,T,E,H,F,M,C,RE>(e, getPersistenceContext(), this.unificationContext);
			pm.merge(this.connection);		
			return e;
		}
		catch (SQLException se) {
			throw new EntityException(se.getMessage());
		} 
		catch (QueryException qe) {
			throw new EntityException(qe.getMessage(), qe);
		}
	}

	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>, 
		H extends ReferenceHolder<A, R, T, E, H, M, C>, 
		F extends EntityFactory<E, H, M, F, C>, 
		M extends EntityMetaData<A, R, T, E, H, F, M, C>, 
		C extends Content,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>,
		Q extends EntityQuery<A, R, T, E, H, F, M, C, RE>
	> 
	E update(E e) throws EntityException {
		PersistenceManager<A,R,T,E,H,F,M,C,RE> pm = new PersistenceManager<A,R,T,E,H,F,M,C,RE>(e, getPersistenceContext(), this.unificationContext);
		pm.update(this.connection);		
		return e;
	}
	
	protected void closing()
		throws DataAccessException {		
	}
	
	protected void closed()
		throws DataAccessException {
	}
	
	@Override
	public final void close()
		throws DataAccessException {
		
		try {
			closing();
		}
		finally {
			if (unificationContext != null) {
				this.unificationContext.close();
				this.unificationContext = null;
			}			
			
			closed();
		}
	}
	
	@Override
	public void commit() throws DataAccessException {
		checkClosed();
		
		try {
			this.connection.commit();			
		}
		catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		finally {
			flush();
		}
	}

	@Override
	public void rollback() throws DataAccessException {
		checkClosed();
		
		try {		
			this.connection.rollback();
		}
		catch (SQLException e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		finally {
			flush();
		}
	}	
	
	private void checkClosed() 
		throws DataAccessException {
		if (this.connection == null) {
			throw new DataAccessException("data access session is closed");
		}
	}

	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>, 
		H extends ReferenceHolder<A, R, T, E, H, M, C>, 
		F extends EntityFactory<E, H, M, F, C>, 
		M extends EntityMetaData<A, R, T, E, H, F, M, C>, 
		C extends Content,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
	> 
	EntityQueryResult<A, R, T, E, H, F, M, C, RE> query(EntityQuery<A, R, T, E, H, F, M, C, RE> query, FetchOptions opts)
			throws EntityException {
		
		try {
			EntityQueryExecutor<A, R, T, E, H, F, M, C, RE> ee = 
					new EntityQueryExecutor<A, R, T, E, H, F, M, C, RE>(getPersistenceContext(), this.unificationContext);
			
			EntityQueryResult<A, R, T, E, H, F, M, C, RE> result = ee.execute(query, opts, this.connection);
			return result;
		} 
		catch (SQLException e) {
			throw new EntityException(e.getMessage());
		} 
		catch (QueryException e) {
			throw new EntityException(e.getMessage(), e);
		}		
	}

	@Override
	public DataObjectQueryResult<DataObject> executeQuery(QueryExpressionSource qes, FetchOptions opts) throws QueryException {
		
		try {
			QueryExecutor qe = new QueryExecutor(getPersistenceContext());
			QueryExpression qx = qes.getQueryExpression();
			DataObjectQueryResult<DataObject> result = qe.execute(qx, opts, this.connection);
			return result;
		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage(), e);
		}
	}
		
	public Connection getConnection() {
		return connection;
	}
	
	@Override
	public EntitySession asEntitySession() {
		return this;
	}
	
	@Override
	public QuerySession asQuerySession() {
		return this;
	}
	
	@Override
	public StatementSession asStatementSession() {
		return this;
	}
	
	
	
	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M, C>, 
		E extends Entity<A, R, T, E, H, F, M, C>, 
		H extends ReferenceHolder<A, R, T, E, H, M, C>, 
		F extends EntityFactory<E, H, M, F, C>, 
		M extends EntityMetaData<A, R, T, E, H, F, M, C>, 
		C extends Content,	
	    
		RE extends EntityQueryElement<A, R, T, E, H, F, M, C, RE>
	> 
	List<E> load(EntityQuery<A, R, T, E, H, F, M, C, RE> query, FetchOptions opts)
			throws EntityException {
	
		EntityQueryResult<A, R, T, E, H, F, M, C, RE> qr = query(query, opts);
		DataObjectQueryResult<EntityDataObject<E>> ol = qr.getResult();				
		List<E> el = new ArrayList<E>(ol.size());
		
		for (EntityDataObject<E> p : ol.getContent()) {
			el.add(p.getRoot());
		}
		
		return el;
	}
	
	@Override
	public void flush() {
		unificationContext.close();
	}
	

	@Override
	public <		
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, ?>,
		E extends Entity<A, R, T, E, H, F, M, ?>,
		H extends ReferenceHolder<A, R, T, E, H, M, ?>,
		F extends EntityFactory<E, H, M, F, ?>,
		M extends EntityMetaData<A, R, T, E, H, F, M, ?>		
	>
	E newEntity(T type) {
		return type.getMetaData().getFactory().newEntity();
	}	
}

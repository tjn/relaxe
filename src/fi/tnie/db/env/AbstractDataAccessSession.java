/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.EntityQueryExecutor;
import fi.tnie.db.PersistenceManager;
import fi.tnie.db.QueryExecutor;
import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.CyclicTemplateException;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.service.DataAccessException;
import fi.tnie.db.service.DataAccessSession;
import fi.tnie.db.service.EntitySession;
import fi.tnie.db.service.QuerySession;
import fi.tnie.db.service.StatementSession;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractDataAccessSession<I extends Implementation<I>>
	implements DataAccessSession, EntitySession, QuerySession, StatementSession {
		
	private Connection connection;
	private I implementation;
	private UnificationContext unificationContext;
	
	public AbstractDataAccessSession(I implementation, Connection connection) {
		super();		
		this.implementation = implementation;
		this.connection = connection;
		this.unificationContext = new SimpleUnificationContext();
	}

	@Override
	public <
		A extends Attribute, R extends Reference, T extends ReferenceType<A, R, T, E, H, F, M, C>, E extends Entity<A, R, T, E, H, F, M, C>, H extends ReferenceHolder<A, R, T, E, H, M, C>, F extends EntityFactory<E, H, M, F, C>, M extends EntityMetaData<A, R, T, E, H, F, M, C>, C extends Content
	> 
	void delete(E e) throws EntityException {						
		PersistenceManager<A,R,T,E,H,F,M,C> pm = new PersistenceManager<A,R,T,E,H,F,M,C>(e, implementation(), this.unificationContext);
		pm.delete(this.connection);
	}

	private I implementation() {
		return this.implementation;
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
		C extends Content
	> 
	E insert(E e) throws EntityException {
		PersistenceManager<A,R,T,E,H,F,M,C> pm = new PersistenceManager<A,R,T,E,H,F,M,C>(e, implementation(), this.unificationContext);
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
		C extends Content
	> 
	E sync(E e) throws EntityException {
		try {
			PersistenceManager<A,R,T,E,H,F,M,C> pm = new PersistenceManager<A,R,T,E,H,F,M,C>(e, implementation(), this.unificationContext);
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
		C extends Content
	> 
	E merge(E e) throws EntityException {
		try {		
			PersistenceManager<A,R,T,E,H,F,M,C> pm = new PersistenceManager<A,R,T,E,H,F,M,C>(e, implementation(), this.unificationContext);
			pm.merge(this.connection);		
			return e;
		}
		catch (SQLException se) {
			throw new EntityException(se.getMessage());
		} 
		catch (CyclicTemplateException ce) {
			throw new EntityException(ce.getMessage());
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
		C extends Content
	> 
	E update(E e) throws EntityException {
		PersistenceManager<A,R,T,E,H,F,M,C> pm = new PersistenceManager<A,R,T,E,H,F,M,C>(e, implementation(), this.unificationContext);
		pm.update(this.connection);		
		return e;
	}
	
	protected void closing()
		throws DataAccessException {		
	}
	
	protected void closed()
		throws DataAccessException {
	}
	
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
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
	> 
	EntityQueryResult<A, R, T, E, H, F, M, C, QT> query(
			EntityQuery<A, R, T, E, H, F, M, C, QT> query, FetchOptions opts)
			throws EntityException {
		
		try {
			EntityQueryExecutor<A, R, T, E, H, F, M, C, QT> ee = new EntityQueryExecutor<A, R, T, E, H, F, M, C, QT>(this.implementation(), this.unificationContext);
			EntityQueryResult<A, R, T, E, H, F, M, C, QT> result = ee.execute(query, opts, this.connection);
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
	public DataObjectQueryResult<DataObject> executeQuery(
			QueryExpressionSource qes, FetchOptions opts) throws QueryException {
		
		try {
			QueryExecutor qe = new QueryExecutor(implementation());
			DataObjectQueryResult<DataObject> result = qe.execute(qes, opts, this.connection);
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
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
	> 
	List<E> load(QT qt, FetchOptions opts)
			throws EntityException {
	
		EntityQueryResult<A, R, T, E, H, F, M, C, QT> qr = query(qt.newQuery(), opts);
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
}

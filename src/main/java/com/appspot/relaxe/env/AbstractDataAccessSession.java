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
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.EntityQueryExecutor;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.QueryExecutor;
import com.appspot.relaxe.StatementExecutor;
import com.appspot.relaxe.ent.AttributeName;
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
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.ResultSetProcessor;
import com.appspot.relaxe.exec.UpdateProcessor;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.SQLDataChangeStatement;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.ddl.SQLSchemaStatement;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;
import com.appspot.relaxe.service.QuerySession;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;


public abstract class AbstractDataAccessSession<I extends Implementation<I>>
	implements DataAccessSession, EntitySession, QuerySession, StatementSession {
	
	private Connection connection;
	
	protected AbstractDataAccessSession(Connection connection) {
		super();
		
		if (connection == null) {
			throw new NullPointerException("connection");
		}
		
		this.connection = connection;
	}

	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	> 
	void delete(E e) throws EntityException {						
		PersistenceManager<A,R,T,E,H,F,M> pm = new PersistenceManager<A,R,T,E,H,F,M>(e, getPersistenceContext(), getUnificationContext());
		pm.delete(getConnection());
	}
	
	protected abstract UnificationContext getUnificationContext();	

	protected abstract PersistenceContext<I> getPersistenceContext();
	
	private Connection getConnection() {
		return this.connection;
	}

	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	> 
	E insert(E e) throws EntityException {
		PersistenceManager<A,R,T,E,H,F,M> pm = new PersistenceManager<A,R,T,E,H,F,M>(e, getPersistenceContext(), getUnificationContext());
		pm.insert(getConnection());
		return e;
	}
	
	
	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	> 
	E load(E e) throws EntityException {
		try {
			PersistenceManager<A,R,T,E,H,F,M> pm = 
					new PersistenceManager<A,R,T,E,H,F,M>(e, getPersistenceContext(), getUnificationContext());
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
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	> 
	E merge(E e) throws EntityException {
		try {		
			PersistenceManager<A,R,T,E,H,F,M> pm = new PersistenceManager<A,R,T,E,H,F,M>(e, getPersistenceContext(), getUnificationContext());
			pm.merge(getConnection());		
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
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	> 
	E update(E e) throws EntityException {
		PersistenceManager<A,R,T,E,H,F,M> pm = new PersistenceManager<A,R,T,E,H,F,M>(e, getPersistenceContext(), getUnificationContext());
		pm.update(getConnection());		
		return e;
	}
	
	protected void closing()
		throws DataAccessException {		
	}
	
	protected void closed()
		throws DataAccessException {		
	}
	
	public boolean isClosed() {
		return (this.connection == null);
	}
	
	@Override
	public final void close()
		throws DataAccessException {
		
		if (this.connection == null) {
			return;
		}
		
		try {
			closing();
		}
		finally {
			this.connection = null;
			closed();
		}
	}
	
	@Override
	public void commit() throws DataAccessException {
		checkClosed();
		
		try {
			getConnection().commit();			
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
			getConnection().rollback();
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
		if (getConnection() == null) {
			throw new DataAccessException("data access session is closed");
		}
	}

	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>
	> 
	EntityQueryResult<A, R, T, E, H, F, M, RE> query(EntityQuery<A, R, T, E, H, F, M, RE> query, FetchOptions opts)
			throws EntityException {
		
		try {
			EntityQueryExecutor<A, R, T, E, H, F, M, RE> ee = 
					new EntityQueryExecutor<A, R, T, E, H, F, M, RE>(getPersistenceContext(), getUnificationContext());
			
			EntityQueryResult<A, R, T, E, H, F, M, RE> result = ee.execute(query, opts, getConnection());
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
	public DataObjectQueryResult<DataObject> execute(QueryExpression qx, FetchOptions opts) throws QueryException {
		
		try {
			QueryExecutor qe = new QueryExecutor(getPersistenceContext());			
			DataObjectQueryResult<DataObject> result = qe.execute(qx, opts, getConnection());
			return result;
		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage(), e);
		}
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
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, H, F, M>, 
		E extends Entity<A, R, T, E, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, H, F, M>,	    
		RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>
	> 
	List<E> list(EntityQuery<A, R, T, E, H, F, M, RE> query, FetchOptions opts)
			throws EntityException {
	
		EntityQueryResult<A, R, T, E, H, F, M, RE> qr = query(query, opts);
		DataObjectQueryResult<EntityDataObject<E>> ol = qr.getResult();				
		List<E> el = new ArrayList<E>(ol.size());
		
		for (EntityDataObject<E> p : ol.getContent()) {
			el.add(p.getRoot());
		}
		
		return el;
	}
	
	@Override
	public void flush() {
		getUnificationContext().close();
	}	

	@Override
	public <		
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,
		M extends EntityMetaData<A, R, T, E, H, F, M>		
	>
	E newEntity(T type) {
		return type.getMetaData().getFactory().newEntity();
	}
	
	
	
		
	@Override
	public void executeSelect(SelectStatement statement, ResultSetProcessor qp)
			throws QueryException {
		try {
			StatementExecutor se = new StatementExecutor(getPersistenceContext());
			se.executeSelect(statement, getConnection(), qp);
		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage());
		}
	}
	
	@Override
	public void execute(Statement statement, QueryProcessor qp)
			throws QueryException {
		try {
			StatementExecutor se = new StatementExecutor(getPersistenceContext());
			se.execute(statement, getConnection(), qp);
		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage());
		}		
	}

	@Override
	public void executeUpdate(SQLDataChangeStatement statement, UpdateProcessor qp)
			throws QueryException {
		try {
			StatementExecutor se = new StatementExecutor(getPersistenceContext());
			se.executeUpdate(statement, getConnection(), qp);
		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage());
		}				
	}

	@Override
	public void execute(SQLSchemaStatement statement) throws QueryException {
		try {
			StatementExecutor se = new StatementExecutor(getPersistenceContext());
			se.execute(statement, getConnection());
		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage());
		}					
	}
	
}

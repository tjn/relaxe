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
package com.appspot.relaxe.rdbms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.appspot.relaxe.AssignmentVisitor;
import com.appspot.relaxe.BlobAssignment;
import com.appspot.relaxe.BlobExtractor;
import com.appspot.relaxe.BlobInputParameter;
import com.appspot.relaxe.EntityQueryExecutor;
import com.appspot.relaxe.InputStreamKey;
import com.appspot.relaxe.InputStreamKeyHolder;
import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.QueryExecutor;
import com.appspot.relaxe.StatementExecutor;
import com.appspot.relaxe.ValueAssignerFactory;
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
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.exec.ResultSetProcessor;
import com.appspot.relaxe.exec.UpdateProcessor;
import com.appspot.relaxe.expr.Assignment;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.Element;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.From;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.expr.Parameter;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.SQLDataChangeStatement;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.UpdateStatement;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.Where;
import com.appspot.relaxe.expr.ddl.SQLSchemaStatement;
import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.query.Query;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryTime;
import com.appspot.relaxe.service.BinaryAttributeReader;
import com.appspot.relaxe.service.BinaryAttributeWriter;
import com.appspot.relaxe.service.ClosableDataAccessSession;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.EntitySession;
import com.appspot.relaxe.service.QueryResultReceiver;
import com.appspot.relaxe.service.QuerySession;
import com.appspot.relaxe.service.Receiver;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.service.UpdateReceiver;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;


public abstract class AbstractDataAccessSession<I extends Implementation<I>>
	implements ClosableDataAccessSession, EntitySession, QuerySession, StatementSession, StatementExecutionSession, 
	BinaryAttributeReader {
	
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
		T extends ReferenceType<A, R, T, E, B, H, F, M>, 
		E extends Entity<A, R, T, E, B, H, F, M>, 
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, B, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	void delete(Entity<A, R, T, E, B, H, F, M> e) throws EntityException {						
		PersistenceManager<A,R,T,E,B,H,F,M> pm = new PersistenceManager<A,R,T,E,B,H,F,M>(e.self(), getPersistenceContext(), getUnificationContext());
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
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	E insert(Entity<A, R, T, E, B, H, F, M> e) throws EntityException {
		PersistenceManager<A,R,T,E,B,H,F,M> pm = new PersistenceManager<A,R,T,E,B,H,F,M>(e.self(), getPersistenceContext(), getUnificationContext());
		pm.insert(getConnection());
		return e.self();
	}
	
	
	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	E load(Entity<A, R, T, E, B, H, F, M> e) throws EntityException {
		try {
			PersistenceManager<A,R,T,E,B,H,F,M> pm = 
					new PersistenceManager<A,R,T,E,B,H,F,M>(e.self(), getPersistenceContext(), getUnificationContext());
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
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	E merge(Entity<A, R, T, E, B, H, F, M> e) throws EntityException {
		try {		
			PersistenceManager<A,R,T,E,B,H,F,M> pm = new PersistenceManager<A,R,T,E,B,H,F,M>(e.self(), getPersistenceContext(), getUnificationContext());
			pm.merge(getConnection());		
			return e.self();
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
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	E update(Entity<A, R, T, E, B, H, F, M> e) throws EntityException {
		PersistenceManager<A,R,T,E,B,H,F,M> pm = new PersistenceManager<A,R,T,E,B,H,F,M>(e.self(), getPersistenceContext(), getUnificationContext());
		pm.update(getConnection());		
		return e.self();
	}
	
	
	
	
	
	protected void closing()
		throws DataAccessException {		
	}
	
	protected void closed()
		throws DataAccessException {		
	}
	
	@Override
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
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
	> 
	EntityQueryResult<A, R, T, E, B, H, F, M, RE> query(EntityQuery<A, R, T, E, B, H, F, M, RE> query, FetchOptions opts)
			throws EntityException {
		
		try {
			EntityQueryExecutor<A, R, T, E, B, H, F, M, RE> ee = 
					new EntityQueryExecutor<A, R, T, E, B, H, F, M, RE>(getPersistenceContext(), getUnificationContext());
			
			EntityQueryResult<A, R, T, E, B, H, F, M, RE> result = ee.execute(query, opts, getConnection());
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
	
	public StatementExecutionSession asStatementExecutionSession() {
		return this;
	}
	
	public BinaryAttributeReader asBinaryAttributeReader() {	
		return this;
	}

	public BinaryAttributeWriter asBinaryAttributeWriter() {	
		return this;
	}

	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,	    
		RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
	> 
	List<E> list(EntityQuery<A, R, T, E, B, H, F, M, RE> query, FetchOptions opts)
			throws EntityException {
	
		EntityQueryResult<A, R, T, E, B, H, F, M, RE> qr = query(query, opts);
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
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>		
	>
	B newEntity(T type) {
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
	public void execute(final Statement statement, final Receiver receiver) 
			throws DataAccessException {
		SelectStatement ss = statement.asSelectStatement();
		
		if (ss != null) {
			executeSelect(ss, receiver);
		}
		else {
			try {			
				QueryProcessor qp = new QueryProcessorAdapter() {
					@Override
					public void updated(int count) {
						receiver.updated(statement, count);
					}				
				};
				
				execute(statement, qp);
			}
			catch (QueryException e) {
				throw new DataAccessException(e.getMessage());
			}
		}		
	}
	
	
	@Override
	public void executeSelect(SelectStatement statement, QueryResultReceiver receiver) throws DataAccessException {
		try {
			QuerySession qs = this;
			DataObjectQueryResult<DataObject> result = qs.execute(statement, null);
			receiver.received(statement, result);
		}
		catch (QueryException e) {
			throw new DataAccessException(e.getMessage());
		}
	}
	
	
	@Override
	public void executeUpdate(SQLDataChangeStatement statement, UpdateReceiver ur) throws DataAccessException {
		try {
			executeUpdate(statement, new UpdateCount(statement, ur));
		}
		catch (QueryException e) {
			throw new DataAccessException(e.getMessage());
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
	public void execute(SQLSchemaStatement statement) throws DataAccessException {
		try {
			StatementExecutor se = new StatementExecutor(getPersistenceContext());
			se.execute(statement, getConnection());
		}
		catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		}					
		catch (QueryException e) {
			throw new DataAccessException(e.getMessage());
		}		
	}	
	
	
	private static final class BlobExtractorAdapter extends QueryProcessorAdapter {
		private final OutputStream out;
		private final BlobExtractor be;
		
		private long nread;

		private BlobExtractorAdapter(OutputStream out, BlobExtractor be) {
			this.out = out;
			this.be = be;
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws QueryException, SQLException {
			try {
				nread = 0;
				this.nread = be.extract(rs, out);
			}
			catch (IOException e) {
				throw new QueryException(e.getMessage(), e);
			}					
		}
		
		public long getBytesRead() {
			return nread;
		}
	}

	private static class UpdateCount
		implements UpdateProcessor {
		
		
		private Statement statement;
		private UpdateReceiver receiver;
		
		public UpdateCount(Statement statement, UpdateReceiver ur) {
			this.statement = statement;
			this.receiver = ur;
		}

		@Override
		public void prepare() throws QueryException {
		}

		@Override
		public void finish() {
		}

		@Override
		public void updated(int count) {
			this.receiver.updated(this.statement, count);			
		}
	}
	
	
	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	long read(Entity<A, R, T, E, B, H, F, M> e, A attribute, final OutputStream out) throws EntityException {
		
		if (out == null) {
			throw new NullPointerException("out");
		}
		
		final M meta = e.getMetaData();
    	
    	TableReference tref = new TableReference(meta.getBaseTable());
    	
    	getPersistenceContext().getValueExtractorFactory();
   	
    	Predicate pkp = getPKPredicate(tref, e);
    	
    	Column col = meta.getColumn(attribute);
    	
    	    	
		StatementExecutor sx = new StatementExecutor(getPersistenceContext());
				
		ValueExpression cr = new ColumnReference(tref, col);						
		Select s = new Select(cr);
		From f = new From(tref);
		Where w = new Where(pkp);
								
		SelectStatement ss = new SelectStatement(s, f, w);
		
		final BlobExtractor be = getPersistenceContext().getBlobExtractorFactory().createBlobExtractor(1);
		BlobExtractorAdapter bea = new BlobExtractorAdapter(out, be);
		
		try {
			sx.executeSelect(ss, this.connection, bea);
		}
		catch (SQLException se) {
			throw new EntityException(se.getMessage(), se);
		} 
		catch (QueryException qe) {
			throw new EntityException(qe.getMessage(), qe);
		}
		
		return bea.getBytesRead();
	}

	@Override
	public <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	long write(Entity<A, R, T, E, B, H, F, M> e, A attribute, final InputStream in) throws EntityException {
		
		if (in == null) {
			throw new NullPointerException("in");
		}
		
		final M meta = e.getMetaData();
    	
    	TableReference tref = new TableReference(meta.getBaseTable());
    	
    	getPersistenceContext().getValueExtractorFactory();
   	
    	Predicate pkp = getPKPredicate(tref, e);
    	
    	Column col = meta.getColumn(attribute);
    	
    	final PersistenceContext<I> pc = getPersistenceContext();
    	
    	final InputStreamKey ik = new InputStreamKey();
    	
    	final BlobInputParameter bin = new BlobInputParameter(col, ik);
    	
    	Assignment a = new Assignment(col.getColumnName(), bin);
    	
    	ElementList<Assignment> assignmentClause = ElementList.newElementList(a);
    	    	    	    	
		StatementExecutor sx = new StatementExecutor(pc) {
			@Override
			protected void preprocess(Statement statement, final PreparedStatement ps) {
				Map<InputStreamKey, InputStream> ism = Collections.singletonMap(ik, in);
				AssignmentVisitor av = new AssignmentVisitor(pc.getValueAssignerFactory(), ps, ism);				
				statement.traverse(null, av);
			}			
		};
				
		UpdateStatement us = new UpdateStatement(tref, assignmentClause, pkp);
		
		try {
			sx.executeUpdate(us, getConnection());
		}
		catch (SQLException se) {
			throw new EntityException(se.getMessage(), se);
		} 
		catch (QueryException qe) {
			throw new EntityException(qe.getMessage(), qe);
		}
		
		return 0; // TODO : real count
	}

	private Predicate getPKPredicate(TableReference tref, Entity<?, ?, ?, ?, ?, ?, ?, ?> pe)
            throws EntityException {

        EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> meta = pe.getMetaData();
        Collection<Column> pkcols = meta.getBaseTable().getPrimaryKey().getColumnMap().values();

        if (pkcols.isEmpty()) {
            throw new EntityException("no pk-columns available for entity type " + pe.type());
        }

        Predicate p = null;

        for (Column col : pkcols) {
            ValueHolder<?, ?, ?> o = pe.get(col);

            // to successfully create a pk predicate
            // every component must be set:
            if (o == null) {
                return null;
            }

            ColumnReference cr = new ColumnReference(tref, col);
            ValueExpression param = createValueExpression(col, o.self());
            p = AndPredicate.newAnd(p, eq(cr, param));
        }

        return p;
    }

    private Predicate eq(ValueExpression a, ValueExpression b) {
    	return Comparison.eq(a, b);        
    }

	private	ValueExpression createValueExpression(Column col, ValueHolder<?, ?, ?> holder) {				
		return newValueExpression(col, holder.self());
	}

	private <
		V extends Serializable,
		P extends ValueType<P>, 
		PH extends ValueHolder<V, P, PH>
	>
	ValueExpression newValueExpression(Column col, ValueHolder<V, P, PH> holder) {				
		return new ImmutableValueParameter<V, P, PH>(col, holder.self());
	}
	 
	
}

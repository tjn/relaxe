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
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.paging.EntityFetcher;
import fi.tnie.db.paging.Receiver;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class SynchronousEntityFetcher<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>	
> implements EntityFetcher<A, R, T, E, H, F, M, C, QT> {
	
	private EntityQueryExecutor<A, R, T, E, H, F, M, C, QT> executor;
	private Connection connection;
	
	public SynchronousEntityFetcher(EntityQueryExecutor<A, R, T, E, H, F, M, C, QT> executor, Connection connection) {
		super();
		this.executor = executor;
		this.connection = connection;
	}

	@Override
	public void fetch(QT queryTemplate, FetchOptions opts, Receiver<EntityQueryResult<A, R, T, E, H, F, M, C, QT>> receiver) 
		throws EntityRuntimeException {
		
		try {
			EntityQuery<A, R, T, E, H, F, M, C, QT> q = queryTemplate.newQuery();
			EntityQueryResult<A, R, T, E, H, F, M, C, QT> qr = executor.execute(q, opts, this.connection);
			receiver.receive(qr);			
		}
		catch (SQLException e) {
			throw new EntityRuntimeException(e.getMessage()); 
		} 
		catch (CyclicTemplateException e) {
			throw new EntityRuntimeException(e.getMessage());
		} 
		catch (QueryException e) {
			throw new EntityRuntimeException(e.getMessage());
		} 
		catch (EntityException e) {
			throw new EntityRuntimeException(e.getMessage());
		}
	}

}

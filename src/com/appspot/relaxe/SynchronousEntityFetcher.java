/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.CyclicTemplateException;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.EntityQueryTemplate;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.paging.EntityFetcher;
import com.appspot.relaxe.paging.PageReceiver;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


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
	public void fetch(QT queryTemplate, FetchOptions opts, PageReceiver<EntityQueryResult<A, R, T, E, H, F, M, C, QT>> receiver, PageReceiver<Throwable> errorReceiver) {		
		try {
			EntityQuery<A, R, T, E, H, F, M, C, QT> q = queryTemplate.newQuery();
			EntityQueryResult<A, R, T, E, H, F, M, C, QT> qr = executor.execute(q, opts, this.connection);
			receiver.receive(qr);			
		}
		catch (SQLException e) {
			errorReceiver.receive(e);
		} 
		catch (CyclicTemplateException e) {
			errorReceiver.receive(e);
		} 
		catch (QueryException e) {
			errorReceiver.receive(e);
		} 
		catch (EntityException e) {
			errorReceiver.receive(e);
		}
	}
}

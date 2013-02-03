/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.ent.CyclicTemplateException;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.QueryExpressionSource;
import fi.tnie.db.paging.DataObjectFetcher;
import fi.tnie.db.paging.Receiver;
import fi.tnie.db.query.QueryException;

public class SynchronousDataObjectFetcher implements DataObjectFetcher {
	
	private QueryExecutor executor;
	private Connection connection;
	
	public SynchronousDataObjectFetcher(QueryExecutor executor, Connection connection) {
		super();
		this.executor = executor;
		this.connection = connection;
	}
	
	@Override
	public void fetch(QueryExpressionSource queryTemplate, FetchOptions opts, Receiver<DataObjectQueryResult<DataObject>> receiver, Receiver<Throwable> errorReceiver) {
		try {
			DataObjectQueryResult<DataObject> qr = executor.execute(queryTemplate, opts, this.connection);
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
	}
}

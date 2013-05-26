/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.ent.CyclicTemplateException;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.QueryExpressionSource;
import com.appspot.relaxe.paging.DataObjectFetcher;
import com.appspot.relaxe.paging.Receiver;
import com.appspot.relaxe.query.QueryException;


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
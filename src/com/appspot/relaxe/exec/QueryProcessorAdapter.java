/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.exec;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.query.QueryException;


public class QueryProcessorAdapter 
	implements QueryProcessor {

	@Override
	public void prepare() {		
	}

	@Override
	public void finish() {
	}

	@Override
	public void process(ResultSet rs, long ordinal)
			throws QueryException, SQLException {
	}

	@Override
	public void abort(Exception e)
		throws QueryException {
		try {
			throw e;
		}
		catch (QueryException qe) {
			throw qe;
		}
		catch (Exception oe) {
			throw new QueryException(oe.getMessage(), oe);
		}		
	}

	@Override
	public void startResultSet(ResultSetMetaData m) 
			throws QueryException, SQLException {
	}

	@Override
	public void endResultSet() throws QueryException {
	}

	@Override
	public void updated(int count) {	
	}
}

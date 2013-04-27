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
	public void abort(Throwable e) {
	}

	@Override
	public void endQuery() throws QueryException {
	}

	@Override
	public void finish() {
	}

	@Override
	public void prepare() {
	}

	@Override
	public void process(ResultSet rs, long ordinal) throws QueryException, SQLException {
				
	}

	@Override
	public void startQuery(ResultSetMetaData m) throws QueryException, SQLException {				
	}

	@Override
	public void updated(int updateCount) {
	}
}

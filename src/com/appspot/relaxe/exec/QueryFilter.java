/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.exec;



import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.query.QueryException;


public class QueryFilter 
	implements QueryProcessor {
	
	private QueryProcessor inner;
	
	public QueryFilter() {
		super();		
	}

	public QueryFilter(QueryProcessor filtered) {
		super();
		this.inner = filtered;
	}
	

	@Override
	public void abort(Exception e) 
		throws QueryException, SQLException {
		this.inner.abort(e);
	}


	@Override
	public void endResultSet() throws QueryException, SQLException {
		this.inner.endResultSet();		
	}


	@Override
	public void finish() {
		this.inner.finish();		
	}


	@Override
	public void prepare() 
			throws QueryException {
		this.inner.prepare();		
	}


	@Override
	public void process(ResultSet rs, long ordinal) throws QueryException, SQLException {
		this.inner.process(rs, ordinal);		
	}


	@Override
	public void startResultSet(ResultSetMetaData m) throws QueryException, SQLException {
		this.inner.startResultSet(m);
		
	}

	@Override
	public void updated(int updateCount) {
		this.inner.updated(updateCount);		
	}
	
	public QueryProcessor getInner() {
		return inner;
	}

	public void setInner(QueryProcessor inner) {
		this.inner = inner;
	}
	
	
	
}


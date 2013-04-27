/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.exec;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.query.QueryException;


public class QueryOffsetProcessor
	extends QueryFilter {
	
	private long offset;	
			
	public QueryOffsetProcessor(QueryProcessor processor, long offset) {
		super(processor);
		this.offset = offset;		
	}

	@Override
	public void process(ResultSet rs, long ordinal) throws QueryException, SQLException {
		if (ordinal >= offset) {			
			getInner().process(rs, ordinal);			
		}
	}
}

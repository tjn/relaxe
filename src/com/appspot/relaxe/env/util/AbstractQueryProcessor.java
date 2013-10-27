/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.query.QueryException;


public abstract class AbstractQueryProcessor 
	extends QueryProcessorAdapter { 
	
	public void apply(ResultSet rs) 
		throws QueryException, SQLException {
		apply(rs, true);
	}
	
	public void apply(ResultSet rs, boolean close) 
		throws QueryException, SQLException {
	
		prepare();
		
		long ordinal = 0;
											
		try {							
			startQuery(rs.getMetaData());
						
			while(rs.next()) {
				process(rs, ++ordinal);
			}			
	
			endQuery();
		}
		catch (Exception e) {
			abort(e);			
		}
		finally {
			if (close) {
				QueryHelper.doClose(rs);
			}
			
			finish();
		}
	}

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
	public void startQuery(ResultSetMetaData m)
			throws QueryException, SQLException {
	}

	@Override
	public void endQuery() throws QueryException {
	}
}

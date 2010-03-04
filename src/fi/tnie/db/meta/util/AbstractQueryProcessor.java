/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import fi.tnie.db.QueryException;
import fi.tnie.db.exec.QueryProcessor;

public abstract class AbstractQueryProcessor implements QueryProcessor {

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
	public void process(ResultSet rs, long ordinal) throws SQLException {
	}

	@Override
	public void startQuery(ResultSetMetaData m) throws SQLException {
	}

	@Override
	public void updated(int updateCount) throws SQLException {
	}
	
	
	public void apply(ResultSet rs) 
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
		catch (SQLException e) {
			abort(e);
			throw e;
		}
		finally {				
			finish();								
		}
	}
	
	
}

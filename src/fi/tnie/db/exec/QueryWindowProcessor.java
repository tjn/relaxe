/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.exec;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.QueryException;

public class QueryWindowProcessor
	extends QueryFilter {
		
	private long limit;
	private long processed = 0;
		
	public QueryWindowProcessor(QueryProcessor processor, long limit) {
		super(processor);
		this.limit = limit;		
	}

	@Override
	public void process(ResultSet rs, long ordinal) throws QueryException, SQLException {
		if ((processed++) < limit) {			
			getInner().process(rs, ordinal);			
		}
	}
}

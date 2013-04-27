/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;

import com.appspot.relaxe.QueryExecutor;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.query.DataObjectQuery;
import com.appspot.relaxe.query.QueryTime;

public abstract class QueryExecutorTest<I extends Implementation<I>>
	extends AbstractUnitTest<I> {
	
	public void testQuery(Table table) throws Exception {		
		Connection c = getContext().newConnection();
						
		DataObjectQuery q = new DataObjectQuery(table);
		DataObjectQueryResult<DataObject> rs = null;
								
		QueryExecutor qe = new QueryExecutor(getPersistenceContext());
		FetchOptions opts = new FetchOptions(10, 10);
		
		rs = qe.execute(q, opts, c);
		
		assertNotNull(rs);
		
		QueryTime times = rs.getElapsed();
		
		logger().debug("testQuery: times=" + times);

		for (DataObject o : rs.getContent()) {
			logger().debug("testQuery: o=" + o);			
			
		}		
	}
	
}


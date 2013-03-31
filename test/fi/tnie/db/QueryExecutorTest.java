/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.meta.Table;
import fi.tnie.db.query.QueryTime;
import fi.tnie.db.query.DataObjectQuery;

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


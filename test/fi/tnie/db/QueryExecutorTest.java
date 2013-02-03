/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.LiteralCatalog.LiteralBaseTable;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryTime;
import fi.tnie.db.query.DataObjectQuery;

public class QueryExecutorTest
	extends AbstractUnitTest {
	
	public void testQuery() throws EntityException, SQLException, QueryException, ClassNotFoundException {
		Implementation imp = getImplementation();
		Connection c = getContext().newConnection();
				
		
		LiteralCatalog.getInstance();
		
		LiteralBaseTable t = LiteralCatalog.LiteralBaseTable.PUBLIC_CITY;
		
		DataObjectQuery q = new DataObjectQuery(t);
		DataObjectQueryResult<DataObject> rs = null;
								
		QueryExecutor qe = new QueryExecutor(imp);
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


/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.SQLException;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.query.QueryException;

public interface HasTestContext {	
	public TestContext getTestContext(Implementation imp) throws SQLException, QueryException;
}

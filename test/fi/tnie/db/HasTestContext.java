/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.SQLException;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.query.QueryException;

public interface HasTestContext<I extends Implementation<I>> {	
	public TestContext<I> getTestContext(I imp) throws SQLException, QueryException;
}

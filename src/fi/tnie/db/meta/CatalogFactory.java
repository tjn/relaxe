/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.QueryException;

public interface CatalogFactory {

	public Catalog create(Connection c)
		throws QueryException, SQLException;
	
	CatalogMap createAll(Connection c)
		throws QueryException, SQLException;
	
	String getCatalogName(Connection c)
	    throws SQLException;
}

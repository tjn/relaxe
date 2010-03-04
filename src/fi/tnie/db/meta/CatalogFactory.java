/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.QueryException;

public interface CatalogFactory {

//	Catalog create(DatabaseMetaData meta, String catalog)
//		throws QueryException, SQLException;
	
	CatalogMap create(Connection c)
		throws QueryException, SQLException;
	
}

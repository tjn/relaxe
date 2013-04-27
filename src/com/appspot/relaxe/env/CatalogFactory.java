/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;


public interface CatalogFactory {

	public Catalog create(Connection c)
		throws QueryException, SQLException;
	
	String getCatalogName(Connection c)
	    throws SQLException;
}

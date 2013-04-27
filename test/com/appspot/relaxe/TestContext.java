/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;


public interface TestContext<I extends Implementation<I>> {

	@Deprecated
    public I getImplementation();
    public PersistenceContext<I> getPersistenceContext();    
    public Connection newConnection() throws SQLException, ClassNotFoundException;    
    public Catalog getCatalog() throws SQLException, QueryException, ClassNotFoundException;
    public String getJdbcURL();
	public Properties getJdbcConfig();
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;


public interface EnvironmentTestContext {

    public Implementation<?> getImplementation();
    public Connection connect() throws SQLException;
    public Driver getDriver();
    public Catalog getCatalog() throws SQLException, QueryException;
    public void setCatalog(Catalog catalog);
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.query.QueryException;

public interface TestContext {

    public Implementation getImplementation();
    public Connection newConnection() throws SQLException, ClassNotFoundException;    
    public Catalog getCatalog() throws SQLException, QueryException, ClassNotFoundException;
}

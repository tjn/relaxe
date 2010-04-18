/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Environment;

public interface TestContext {

    public Environment getEnvironment();
    public Connection connect() throws SQLException;
    public Driver getDriver();
    public Catalog getCatalog();
    public void setCatalog(Catalog catalog);
    
}

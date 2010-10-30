/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;

import fi.tnie.db.ent.EntityException;

public interface Persistable {

    void insert(Connection c)
        throws EntityException;

    void update(Connection c)
        throws EntityException;

    void delete(Connection c)
        throws EntityException;
}

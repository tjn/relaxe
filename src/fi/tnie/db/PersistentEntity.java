/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;

public interface PersistentEntity<
    A extends Enum<A> & Identifiable,
    R extends Enum<R> & Identifiable,
    Q extends Enum<Q> & Identifiable,
    P extends PersistentEntity<A, R, Q, ? extends P>
>
    extends Entity<A, R, Q, P> {

    void insert(Connection c)
        throws EntityException;

    void update(Connection c)
        throws EntityException;

    void delete(Connection c)
        throws EntityException;
}

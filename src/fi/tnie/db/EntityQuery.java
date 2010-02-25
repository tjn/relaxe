/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;

import fi.tnie.db.exec.QueryFilter;

public interface EntityQuery<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable, 
	Q extends Enum<Q> & Identifiable, 
	E extends Entity<A, R, Q, ? extends E>
>
{
	EntityQueryResult<A, R, Q, E> exec(Connection c)
			throws EntityQueryException;

	EntityQueryResult<A, R, Q, E> exec(long offset, Long limit,
			Connection c) throws EntityQueryException;

	EntityQueryResult<A, R, Q, E> exec(QueryFilter qf, Connection c)
			throws EntityQueryException;

}
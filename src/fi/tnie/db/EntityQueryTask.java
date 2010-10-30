/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityQueryException;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.Identifiable;
import fi.tnie.db.exec.QueryFilter;
import fi.tnie.db.types.ReferenceType;

public interface EntityQueryTask<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable, 
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
>
{
	EntityQueryResult<A, R, Q, T, E> exec(Connection c)
			throws EntityQueryException;

	EntityQueryResult<A, R, Q, T, E> exec(long offset, Long limit,
			Connection c) throws EntityQueryException;

	EntityQueryResult<A, R, Q, T, E> exec(QueryFilter qf, Connection c)
			throws EntityQueryException;

}
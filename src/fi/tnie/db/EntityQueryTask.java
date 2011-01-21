/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;
import java.sql.Connection;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityQueryException;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.exec.QueryFilter;
import fi.tnie.db.types.ReferenceType;

public interface EntityQueryTask<
	A extends Serializable,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
{
	EntityQueryResult<A, R, T, E> exec(Connection c)
			throws EntityQueryException;

	EntityQueryResult<A, R, T, E> exec(long offset, Long limit,
			Connection c) throws EntityQueryException;

	EntityQueryResult<A, R, T, E> exec(QueryFilter qf, Connection c)
			throws EntityQueryException;
}
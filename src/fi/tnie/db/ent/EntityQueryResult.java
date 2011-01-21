/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.List;

import fi.tnie.db.types.ReferenceType;

public interface EntityQueryResult<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
> {
	E first();
	long getAvailable();
	List<E> getContent();
	boolean isEmpty();
	EntityQuery<A, R, T, E> getSource();
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.List;

import fi.tnie.db.types.ReferenceType;

public interface EntityQueryResult<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
> {
	E first();
	long getAvailable();
	List<E> getContent();
	boolean isEmpty();
	EntityQuery<A, R, Q, T, E> getSource();
}

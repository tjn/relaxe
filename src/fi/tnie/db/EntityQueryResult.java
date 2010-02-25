/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.List;

public interface EntityQueryResult<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
> {
	E first();
	long getAvailable();
	List<E> getContent();
	boolean isEmpty();
	DefaultEntityQuery<A, R, Q, E> getSource();
}

/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public abstract class AbstractEntityMetaData<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, E>
> 
	implements EntityMetaData<A, R, Q, E>
{
	
}

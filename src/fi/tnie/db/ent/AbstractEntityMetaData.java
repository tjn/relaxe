/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityMetaData<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>, 
	E extends Entity<A, R, Q, T, E>
> 
	implements EntityMetaData<A, R, Q, T, E>
{
	
}

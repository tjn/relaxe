/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityMetaData<
	A extends Serializable,
	R, 
	T extends ReferenceType<T>, 
	E extends Entity<A, R, T, E>
> 
	implements EntityMetaData<A, R, T, E>
{	
}

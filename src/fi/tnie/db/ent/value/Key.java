/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.Type;

public interface Key<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,	
	E extends Entity<A, R, T, E>,
	V extends Type<V>,
	K extends Key<A, R, T, E, V, K>
>
	extends Serializable
{
	V type();
}

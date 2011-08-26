/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.im;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.types.ReferenceType;

public interface EntityIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, ?>,
	E extends Entity<A, R, T, E, ?, ?, ?>
> 
	extends IdentityMap<E>
{
}

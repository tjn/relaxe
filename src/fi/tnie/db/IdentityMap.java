/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.EntityRuntimeException;

public interface IdentityMap<V> {
	V get(V v)
		throws EntityRuntimeException;
}

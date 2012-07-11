/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.im;

import fi.tnie.db.ent.EntityRuntimeException;

public interface IdentityMap<K, V> {
	V get(K key)
		throws EntityRuntimeException;
}

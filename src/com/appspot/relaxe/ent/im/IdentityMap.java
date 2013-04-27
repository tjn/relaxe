/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.im;

import com.appspot.relaxe.ent.EntityRuntimeException;

public interface IdentityMap<K, V> {
	V get(K key)
		throws EntityRuntimeException;
}

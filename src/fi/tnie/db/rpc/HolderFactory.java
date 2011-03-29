/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

import fi.tnie.db.types.Type;

public interface HolderFactory<V extends Serializable, T extends Type<T>, H extends Holder<V, T>> {
	H newHolder(V value);
		 
}

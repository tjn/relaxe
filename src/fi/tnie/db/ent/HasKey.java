/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.ent.value.Key;
import fi.tnie.db.types.Type;

public interface HasKey<			
	V extends Type<V>,
	K extends Key<E, V, K>,
	E extends HasKey<V, K, E>
> {

}

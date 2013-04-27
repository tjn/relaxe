/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.ent.value.Key;
import com.appspot.relaxe.types.Type;

public interface HasKey<			
	V extends Type<V>,
	K extends Key<E, V, K>,
	E extends HasKey<V, K, E>
> {

}

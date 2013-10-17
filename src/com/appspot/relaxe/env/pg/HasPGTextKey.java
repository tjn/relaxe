/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.value.HasString;

public interface HasPGTextKey<
	A extends Attribute,
	E extends HasPGText<A, E> & HasString<A, E>
> {

	PGTextKey<A, E> getPGTextKey(A a);	
	void register(PGTextKey<A, E> key);
}

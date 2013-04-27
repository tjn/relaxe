/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.env.pg.PGTextArrayKey;

public interface HasPGTextArrayKey<
	A extends Attribute,
	E extends HasPGTextArray<A, E>	
>	{

	PGTextArrayKey<A, E> getPGTextArrayKey(A a);	
	void register(PGTextArrayKey<A, E> key);
}

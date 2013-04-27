/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.Attribute;

public interface HasMPAARatingKey<
	A extends Attribute,
	E extends HasMPAARating<A, E>	
>	{

	MPAARatingKey<A, E> getMPAARatingKey(A a);	
	void register(MPAARatingKey<A, E> key);
}

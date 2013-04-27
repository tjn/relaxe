/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.env.pg.PGTextArrayHolder;
import com.appspot.relaxe.env.pg.PGTextArrayKey;

public interface HasPGTextArray<
	A extends Attribute, 
	E extends HasPGTextArray<A, E>
> {

	void setPGTextArray(PGTextArrayKey<A, E> k, PGTextArrayHolder newValue);
	
	PGTextArrayHolder getPGTextArray(PGTextArrayKey<A, E> k);
	

}

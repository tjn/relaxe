/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.Attribute;

public interface HasMPAARating<
	A extends Attribute, 
	E extends HasMPAARating<A, E>
> {

	void setMPAARating(MPAARatingKey<A, E> k, MPAARatingHolder newValue);
	
	MPAARatingHolder getMPAARating(MPAARatingKey<A, E> k);
	

}

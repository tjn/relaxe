/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.value.HasString;

public interface HasPGText<
	A extends Attribute, 
	E extends HasPGText<A, E> & HasString<A, E>
> {

	void setPGText(PGTextKey<A, E> k, PGTextHolder newValue);
	
	PGTextHolder getPGText(PGTextKey<A, E> k);
	

}

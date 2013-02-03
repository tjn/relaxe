/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ent.Attribute;

public interface HasPGTextArray<
	A extends Attribute, 
	E extends HasPGTextArray<A, E>
> {

	void setPGTextArray(PGTextArrayKey<A, E> k, PGTextArrayHolder newValue);
	
	PGTextArrayHolder getPGTextArray(PGTextArrayKey<A, E> k);
	

}

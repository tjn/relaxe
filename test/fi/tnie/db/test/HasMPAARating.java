/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ent.Attribute;

public interface HasMPAARating<
	A extends Attribute, 
	E extends HasMPAARating<A, E>
> {

	void setMPAARating(MPAARatingKey<A, E> k, MPAARatingHolder newValue);
	
	MPAARatingHolder getMPAARating(MPAARatingKey<A, E> k);
	

}

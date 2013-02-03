/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;

import fi.tnie.db.ent.Attribute;

public interface HasMPAARatingKey<
	A extends Attribute,
	E extends HasMPAARating<A, E>	
>	{

	MPAARatingKey<A, E> getMPAARatingKey(A a);	
	void register(MPAARatingKey<A, E> key);
}
